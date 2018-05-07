Const RM2_TEXTURES% = 1
Const RM2_OPAQUE% = 2
Const RM2_ALPHA% = 3
Const RM2_INVISIBLE% = 4
Const RM2_SCREEN% = 5
Const RM2_WAYPOINT% = 6
Const RM2_POINTLIGHT% = 7
Const RM2_SPOTLIGHT% = 8
Const RM2_SOUNDEMITTER% = 9
Const RM2_PROP% = 10

Const RM2_LOADFLAG_COLOR% = 1
Const RM2_LOADFLAG_ALPHA% = 2

Const RM2_BLENDFLAG_NORMAL% = 0
Const RM2_BLENDFLAG_DIFFUSE% = 1
Const RM2_BLENDFLAG_LM% = 2

Function ReadByteString$(stream%)
	Local retVal$ = ""
	Local length% = ReadByte(stream)
	For i% = 1 To length
		retVal=retVal+Chr(ReadByte(stream))
	Next
	Return retVal
End Function

Function LoadRM2(fullFilename$,rt.RoomTemplates)
	DebugLog fullFilename
	
	Local obj% = CreatePivot()
	Local opaqueMesh% = CreateMesh()
	EntityParent(opaqueMesh,obj)
	Local alphaMesh% = 0
	
	Local usedTextures.IntArrayList = CreateIntArrayList()
	Local collisionObjs.IntArrayList = CreateIntArrayList()
	
	Local filename$ = StripPath(fullFilename)
	Local filepath$ = StripFilename(fullFilename)
	
	Local file% = ReadFile(fullFilename)
	
	Local header$ = ""
	For i% = 0 To 3
		header=header+Chr(ReadByte(file))
	Next
	
	If header<>".RM2" Then
		RuntimeError("Error while loading "+fullFilename+": expected .RM2, found "+header)
	EndIf
	
	Local partType%
	
	Local count%
	Local texName$
	Local flags%
	Local loadFlags%
	Local blendFlags%
	Local uvSet%
	Local texture%
	Local shouldLoadTexture%
	
	Local mesh%
	Local clonedMesh%
	Local brush%
	Local textureIndex%[2]
	Local layerCount%
	Local mat.Materials
	Local surf%
	Local x#
	Local y#
	Local z#
	Local r%
	Local g%
	Local b%
	Local u#
	Local v#
	Local vert1%
	Local vert2%
	Local vert3%
	
	Local tempScreen.TempScreens
	Local waypointTemp.TempWaypoints
	
	Local range#
	Local intensity#
	
	Local pitch#
	Local yaw#
	Local roll#
	
	Local innerConeAngle#
	Local outerConeAngle#
	
	Local lightTemplate.LightTemplates
	
	Local ambienceInd%
	
	Local propName$
	Local xScale#
	Local yScale#
	Local zScale#
	Local prop%
	
	Local prevType%
	While Not Eof(file)
		prevType = partType
		partType = ReadByte(file)
		If Instr(fullFilename,"closet")>0 Then
			DebugLog "parttype: "+partType
		EndIf
		Select partType
			Case RM2_TEXTURES
				;[Block]
				count = ReadByte(file)
				For i% = 0 To count-1
					texName = ReadByteString(file)
					flags = ReadByte(file)
					loadFlags = flags Shr 4
					blendFlags = flags And $0F
					uvSet = ReadByte(file)
					
					mat = GetCache(texName)
					If mat=Null Then
						shouldLoadTexture=True
					ElseIf mat\Diff=0 Then
						shouldLoadTexture=True
					EndIf
					
					If shouldLoadTexture Then
						If blendFlags=RM2_BLENDFLAG_NORMAL Then
							blendFlags = 2
						ElseIf blendFlags=RM2_BLENDFLAG_DIFFUSE Then
							blendFlags = 5
						ElseIf blendFlags=RM2_BLENDFLAG_LM Then
							blendFlags = 3
						EndIf
						
						texture = LoadRMeshTexture(filepath,texName,loadFlags)
						TextureBlend(texture,blendFlags)
						TextureCoords(texture,uvSet)
						AddTextureToCache(texture)
					EndIf
					PushIntArrayListElem(usedTextures,Handle(GetCache(texName)))
				Next
				;[End Block]
			Case RM2_OPAQUE,RM2_ALPHA
				;[Block]
				mesh = CreateMesh()
				brush = 0
				
				layerCount = 0
				For i% = 0 To 1
					textureIndex[i] = ReadByte(file)
					If textureIndex[i]>0 Then layerCount=layerCount+1
				Next
				
				For i% = 0 To 1
					mat = Null
					If textureIndex[i]>0 Then
						mat = Object.Materials(GetIntArrayListElem(usedTextures,textureIndex[i]-1))
					EndIf
					If mat<>Null Then
						If brush=0 Then brush = CreateBrush(255,255,255)
						BrushTexture(brush,mat\Diff,0,i+(layerCount=2)) ;TODO: replace this hack once we can start using shaders
					EndIf
				Next
				
				If brush<>0 And (layerCount=2) Then
					BrushTexture brush,AmbientLightRoomTex,0,0
				EndIf
				
				surf = CreateSurface(mesh)
				If brush<>0 Then
					PaintSurface surf,brush
					FreeBrush brush
				EndIf
				
				;vertices
				count = ReadShort(file)
				For i%=0 To count-1
					x = ReadFloat(file) : y = ReadFloat(file) : z = ReadFloat(file)
					
					r = ReadByte(file) : g = ReadByte(file) : b = ReadByte(file)
					
					AddVertex(surf,x,y,z)
					VertexColor(surf,i,r,g,b,1.0)
					For j%=0 To 1
						u = ReadFloat(file) : v = ReadFloat(file)
						VertexTexCoords(surf,i,u,v,0.0,j)
					Next
				Next
				
				;triangles
				count = ReadShort(file)
				For i%=0 To count-1
					vert1 = ReadShort(file)
					vert2 = ReadShort(file)
					vert3 = ReadShort(file)
					
					AddTriangle(surf,vert1,vert2,vert3)
				Next
				
				If partType=RM2_OPAQUE Then
					AddMesh(mesh,opaqueMesh)
				ElseIf partType=RM2_ALPHA Then
					If alphaMesh=0 Then alphaMesh = CreateMesh()
					AddMesh(mesh,alphaMesh)
				EndIf
				EntityParent(mesh,obj)
				EntityPickMode(mesh,2,True)
				EntityFX(mesh,1+2)
				
				;double-sided collision bois
				clonedMesh = CopyMesh(mesh)
				FlipMesh(clonedMesh)
				AddMesh(clonedMesh,mesh)
				FreeEntity(clonedMesh)
				
				EntityAlpha(mesh,0.0)
				
				EntityType mesh,HIT_MAP
				PushIntArrayListElem(collisionObjs,mesh)
				;[End Block]
			Case RM2_INVISIBLE
				;[Block]
				mesh = CreateMesh()
				
				surf = CreateSurface(mesh)
				
				;vertices
				count = ReadShort(file)
				For i%=0 To count-1
					x = ReadFloat(file) : y = ReadFloat(file) : z = ReadFloat(file)
					
					AddVertex(surf,x,y,z)
				Next
				
				;triangles
				count = ReadShort(file)
				For i%=0 To count-1
					vert1 = ReadShort(file)
					vert2 = ReadShort(file)
					vert3 = ReadShort(file)
					
					AddTriangle(surf,vert1,vert2,vert3)
				Next
				
				EntityParent(mesh,obj)
				EntityAlpha(mesh,1.0)
				EntityType mesh,HIT_MAP
				PushIntArrayListElem(collisionObjs,mesh)
				
				If Instr(fullFilename,"closet")>0 Then
					DebugLog "INVISIBLE"
					If Eof(file) Then RuntimeError "AAAA"
				EndIf
				;[End Block]
			Case RM2_SCREEN
				;[Block]
				tempScreen = New TempScreens	
				tempScreen\x = ReadFloat(file)*RoomScale
				tempScreen\y = ReadFloat(file)*RoomScale
				tempScreen\z = ReadFloat(file)*RoomScale
				tempScreen\imgpath = ReadByteString(file)
				tempScreen\roomtemplate = rt
				;[End Block]
			Case RM2_WAYPOINT
				;[Block]
				If Instr(fullFilename,"closet")>0 Then
					DebugLog "WAYPOINT"
				EndIf
				waypointTemp = New TempWaypoints
				waypointTemp\x = ReadFloat(file)*RoomScale
				waypointTemp\y = ReadFloat(file)*RoomScale
				waypointTemp\z = ReadFloat(file)*RoomScale
				For i% = 0 To 16
					waypointTemp\connectedTo[i] = ReadByte(file)
					If waypointTemp\connectedTo[i]=0 Then Exit
				Next
				waypointTemp\roomTemplate = rt
				;did some waypoint-based lifeform just say... ICE
				cuboid% = CreateCube()
				ScaleMesh(cuboid,100.0,100.0,100.0)
				PositionMesh(cuboid,waypointTemp\x/RoomScale,waypointTemp\y/RoomScale,waypointTemp\z/RoomScale)
				AddMesh(cuboid,opaqueMesh)
				FreeEntity cuboid
				;GET ICCED DUMMY
				;RT FOR FREE IPOD
				;you may now access
				;         ^
				;        /|\
				;       / | \				;      <--+-->
				;       \ | /
				;        \|/
				;         v
				;   THE OCTAHEDRON
				;[End Block]
			Case RM2_POINTLIGHT
				;[Block]
				x = ReadFloat(file)*RoomScale
				y = ReadFloat(file)*RoomScale
				z = ReadFloat(file)*RoomScale
				
				range = (ReadFloat(file)/8.0)*RoomScale
				
				r = ReadByte(file)
				g = ReadByte(file)
				b = ReadByte(file)
				intensity = Float(ReadByte(file))/255.0
				
				AddTempLight(rt, x,y,z, LIGHTTYPE_POINT, range, r,g,b)
				;[End Block]
			Case RM2_SPOTLIGHT
				;[Block]
				x = ReadFloat(file)*RoomScale
				y = ReadFloat(file)*RoomScale
				z = ReadFloat(file)*RoomScale
				
				range = (ReadFloat(file)/8.0)*RoomScale
				
				r = ReadByte(file)
				g = ReadByte(file)
				b = ReadByte(file)
				intensity = Float(ReadByte(file))/255.0
				
				pitch = ReadFloat(file)
				yaw = ReadFloat(file)
				
				innerConeAngle = ReadFloat(file)
				outerConeAngle = ReadFloat(file)
				
				lightTemplate = AddTempLight(rt, x,y,z, LIGHTTYPE_SPOT, range, r,g,b)
				
				lightTemplate\pitch = pitch
				lightTemplate\yaw = yaw
				lightTemplate\innerConeAngle = innerConeAngle
				lightTemplate\innerConeAngle = outerConeAngle
				;[End Block]
			Case RM2_SOUNDEMITTER
				;[Block]
				x = ReadFloat(file)*RoomScale
				y = ReadFloat(file)*RoomScale
				z = ReadFloat(file)*RoomScale
				
				ambienceInd = ReadByte(file)
				
				range = ReadFloat(file)
				
				For j = 0 To MaxRoomEmitters-1
					If rt\TempSoundEmitter[j]=0 Then
						rt\TempSoundEmitterX[j]=x
						rt\TempSoundEmitterY[j]=y
						rt\TempSoundEmitterZ[j]=z
						rt\TempSoundEmitter[j]=ambienceInd
						
						rt\TempSoundEmitterRange[j]=range
						temp1i=1
						Exit
					EndIf
				Next
				;[End Block]
			Case RM2_PROP
				;[Block]
				propName = ReadByteString(file)
				
				x = ReadFloat(file)*RoomScale
				y = ReadFloat(file)*RoomScale
				z = ReadFloat(file)*RoomScale
				
				pitch = ReadFloat(file)
				yaw = ReadFloat(file)
				roll = ReadFloat(file)
				
				xScale = ReadFloat(file)
				yScale = ReadFloat(file)
				zScale = ReadFloat(file)
				
				prop = CreatePropObj("GFX/Map/Props/"+propName)
				
				PositionEntity prop,x,y,z
				RotateEntity prop,pitch,yaw,roll
				ScaleEntity prop,temp1,temp2,temp3
				
				EntityParent prop,obj
				EntityType prop,HIT_MAP
				EntityPickMode prop,2
				;[End Block]
			Default
				RuntimeError "Error after reading type "+prevType
		End Select
	Wend
	
	DeleteIntArrayList(usedTextures)
	
	EntityFX(opaqueMesh,1+2)
	If alphaMesh<>0 Then
		EntityParent(alphaMesh,opaqueMesh)
		EntityFX(alphaMesh,1+2+16)
	EndIf
	
	rt\obj = obj
	rt\collisionObjs = collisionObjs
	
	CloseFile(file)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D