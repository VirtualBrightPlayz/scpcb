
Include "Array.bb"

Function Min#(a#,b#)
	If a<b Then Return a
	Return b
End Function

Function Max#(a#,b#)
	If a>b Then Return a
	Return b
End Function

Include "INI.bb"

;RMESH STUFF;;;;

Type Materials
	Field name$
	Field loadflags%
	Field blendflags%
	Field uvSet%
	Field Diff
	
	Field StepSound%
End Type

Function StripFilename$(file$)
	Local mi$=""
	Local lastSlash%=0
	If Len(file)>0
		For i%=1 To Len(file)
			mi=Mid(file$,i,1)
			If mi="\" Or mi="/" Then
				lastSlash=i
			EndIf
		Next
	EndIf
	
	Return Left(file,lastSlash)
End Function

Function StripPath$(file$) 
	Local name$=""
	If Len(file$)>0 
		For i=Len(file$) To 1 Step -1 
			
			mi$=Mid$(file$,i,1) 
			If mi$="\" Or mi$="/" Then Return name$
			
			name$=mi$+name$ 
		Next 
		
	EndIf 
	
	Return name$ 
End Function

Function Piece$(s$,entry,char$=" ")
	While Instr(s,char+char)
		s=Replace(s,char+char,char)
	Wend
	For n=1 To entry-1
		p=Instr(s,char)
		s=Right(s,Len(s)-p)
	Next
	p=Instr(s,char)
	If p<1
		a$=s
	Else
		a=Left(s,p-1)
	EndIf
	Return a
End Function

Function KeyValue$(entity,key$,defaultvalue$="")
	properties$=EntityName(entity)
	properties$=Replace(properties$,Chr(13),"")
	key$=Lower(key)
	Repeat
		p=Instr(properties,Chr(10))
		If p Then test$=(Left(properties,p-1)) Else test=properties
		testkey$=Piece(test,1,"=")
		testkey=Trim(testkey)
		testkey=Replace(testkey,Chr(34),"")
		testkey=Lower(testkey)
		If testkey=key Then
			value$=Piece(test,2,"=")
			value$=Trim(value$)
			value$=Replace(value$,Chr(34),"")
			Return value
		EndIf
		If Not p Then Return defaultvalue$
		properties=Right(properties,Len(properties)-p)
	Forever 
End Function

Function GetTextureFromCache%(name$)
	For tc.Materials=Each Materials
		If tc\name = name Then Return tc\Diff
	Next
	Return 0
End Function

Function GetCache.Materials(name$)
	For tc.Materials=Each Materials
		If tc\name = name Then Return tc
	Next
	Return Null
End Function

Function AddTextureToCache(texture%,loadflags%,blendflags%,uvSet%)
	If Trim(StripPath(TextureName(texture)))="" Then Return
	Local tc.Materials=GetCache(StripPath(TextureName(texture)))
	;DebugLog StripPath(TextureName(texture))
	If tc.Materials=Null Then
		tc.Materials=New Materials
		tc\name=StripPath(TextureName(texture))
		tc\loadflags=loadflags
		tc\blendflags=blendflags
		tc\uvSet=uvSet
		tc\Diff=0
	EndIf
	If tc\Diff=0 Then tc\Diff=texture
End Function

Function ClearTextureCache()
	For tc.Materials=Each Materials
		If tc\Diff<>0 Then FreeTexture tc\Diff
		;If tc\Bump<>0 Then FreeTexture tc\Bump
		Delete tc
	Next
End Function

Function FreeTextureCache()
	For tc.Materials=Each Materials
		If tc\Diff<>0 Then FreeTexture tc\Diff
		;If tc\Bump<>0 Then FreeTexture tc\Bump
		tc\Diff = 0; : tc\Bump = 0
	Next
End Function

Function LoadRMeshTexture%(roompath$,name$,flags%)
	If FileType(roompath+name)=1 Then
		Return LoadTexture(roompath+name,flags)
	Else
		Return LoadTexture("GFX/map/"+name,flags) ;TODO: don't hardcode?
	EndIf
End Function

Type WaypointTemp
	Field x#
	Field y#
	Field z#
	
	Field pivot%
	
	Field connectedTo%[32]
End Type

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

Function WriteByteString(stream%,stri$)
	WriteByte(stream,Len(stri))
	For i% = 1 To Len(stri)
		WriteByte(stream,Asc(Mid(stri,i,1)))
	Next
End Function

Function ReadByteString(stream%)
	Local length% = ReadByte(stream)
	Local retVal$ = ""
	For i% = 1 To length
		retVal=retVal+Chr(ReadByte(stream))
	Next
End Function

Function CreateWaypoint(x#,y#,z#)
	wpt.WaypointTemp = New WaypointTemp
	
	wpt\x = x
	wpt\y = y
	wpt\z = z
	
	wpt\pivot = CreatePivot()
	;ScaleEntity wpt\pivot,20.0,20.0,20.0
	;EntityColor wpt\pivot,Rand(100,255),Rand(100,255),Rand(100,255)
	;RotateEntity wpt\pivot,Rand(0,360),Rand(0,360),Rand(0,360)
	PositionEntity wpt\pivot,wpt\x,wpt\y,wpt\z,True
	EntityRadius wpt\pivot,20.0
	EntityPickMode wpt\pivot,1,True
End Function

Function EntityVisibleLazy(ent1%,ent2%)
	Local originalX# = EntityX(ent1,True)
	Local originalY# = EntityY(ent1,True)
	Local originalZ# = EntityZ(ent1,True)
	Local threshold# = 5.0
	If EntityVisible(ent1,ent2) Then Return True
	
	PositionEntity ent1,originalX,originalY,originalZ,True
	TranslateEntity ent1,threshold,0,0
	If EntityVisible(ent1,ent2) Then
		PositionEntity ent1,originalX,originalY,originalZ,True
		Return True
	EndIf
	
	PositionEntity ent1,originalX,originalY,originalZ,True
	TranslateEntity ent1,-threshold,0,0
	If EntityVisible(ent1,ent2) Then
		PositionEntity ent1,originalX,originalY,originalZ,True
		Return True
	EndIf
	
	PositionEntity ent1,originalX,originalY,originalZ,True
	TranslateEntity ent1,0,0,threshold
	If EntityVisible(ent1,ent2) Then
		PositionEntity ent1,originalX,originalY,originalZ,True
		Return True
	EndIf
	
	PositionEntity ent1,originalX,originalY,originalZ,True
	TranslateEntity ent1,0,0,-threshold
	If EntityVisible(ent1,ent2) Then
		PositionEntity ent1,originalX,originalY,originalZ,True
		Return True
	EndIf
	
	PositionEntity ent1,originalX,originalY,originalZ,True
	Return False
End Function

Function LoadRMesh(file$)
	;generate a texture made of white
	Local blankTexture%
	blankTexture=CreateTexture(4,4,1,1)
	ClsColor 255,255,255
	SetBuffer TextureBuffer(blankTexture)
	Cls
	SetBuffer BackBuffer()
	
	Local pinkTexture%
	pinkTexture=CreateTexture(4,4,1,1)
	ClsColor 255,255,255
	SetBuffer TextureBuffer(pinkTexture)
	Cls
	SetBuffer BackBuffer()
	
	ClsColor 0,0,0
	
	;read the file
	Local f%=ReadFile(file)
	Local i%,j%,k%,x#,y#,z#,yaw#
	Local vertex%
	Local temp1i%,temp2i%,temp3i%
	Local temp1#,temp2#,temp3#
	Local temp1s$, temp2s$
	
	Local hasTriggerBox% = False
	
	For i=0 To 3 ;reattempt up to 3 times
		If f=0 Then
			f=ReadFile(file)
		Else
			Exit
		EndIf
	Next
	If f=0 Then RuntimeError "Error reading file "+Chr(34)+file+Chr(34)
	Local isRMesh$ = ReadString(f)
	If isRMesh$="RoomMesh"
		;Continue
	ElseIf isRMesh$="RoomMesh.HasTriggerBox"
		hasTriggerBox% = True
	Else
		RuntimeError Chr(34)+file+Chr(34)+" is Not RMESH ("+isRMesh+")"
	EndIf
	
	Local origFile$ = file
	file=StripFilename(file)
	
	Local count%,count2%
	
	;drawn meshes
	Local Opaque%,Alpha%
	
	Opaque=CreateMesh()
	Alpha=CreateMesh()
	
	count = ReadInt(f)
	Local childMesh%
	Local surf%,tex%[2],brush%
	
	Local isAlpha%
	
	Local u#,v#
	
	For i=1 To count ;drawn mesh
		childMesh=CreateMesh()
		
		surf=CreateSurface(childMesh)
		
		brush=CreateBrush()
		
		tex[0]=0 : tex[1]=0
		
		isAlpha=0
		For j=0 To 1
			temp1i=ReadByte(f)
			If temp1i<>0 Then
				temp1s=ReadString(f)
				temp1s = Replace(temp1s,"room2cz3","hll_bench_2c") ;nice hack lol
				temp1s = Replace(temp1s,"room2ctunnel","tnnl_plain_2c") ;nice hack lol
				temp1s = Replace(temp1s,"room2c","hll_plain_2c") ;nice hack lol
				temp1s = Replace(temp1s,".bmp",".png")
				tex[j]=GetTextureFromCache(temp1s)
				loadflags% = RM2_LOADFLAG_COLOR
				blendflags% = RM2_BLENDFLAG_NORMAL
				If tex[j]=0 Then ;texture is not in cache
					Select True
						Case temp1i<3
							tex[j]=LoadRMeshTexture(file,temp1s,1)
							loadflags = RM2_LOADFLAG_COLOR
						Default
							tex[j]=LoadRMeshTexture(file,temp1s,3)
							loadflags = RM2_LOADFLAG_COLOR Or RM2_LOADFLAG_ALPHA
					End Select
					
					If tex[j]<>0 Then
						If temp1i=1 Then ;wtf does this mean????
							TextureBlend tex[j],5
							blendflags = RM2_BLENDFLAG_DIFFUSE
						EndIf
						If Instr(Lower(temp1s),"_lm")<>0 Then
							TextureBlend tex[j],3
							blendflags = RM2_BLENDFLAG_LM
						EndIf
					ElseIf temp1s="labelcont_173_1.jpg" Then
						temp1s = "label173.jpg"
					ElseIf temp1s="labelcont_1123_2.png" Then
						temp1s = "label1123.png"
					ElseIf temp1s="labelcont_008_1.jpg" Then
						temp1s = "label008.jpg"
					ElseIf temp1s="door" Then ;what
						temp1s = "wood.jpg"
					ElseIf temp1s<>"" Then
						RuntimeError temp1s+" don't exist!"
					EndIf
					
				EndIf
				If tex[j]<>0 Then
					isAlpha=2
					If temp1i=3 Then isAlpha=1
					
					TextureCoords tex[j],1-j
					
					If isAlpha=1 Then
						If j=1 Then
							blendflags = RM2_BLENDFLAG_NORMAL
						EndIf
					EndIf
					AddTextureToCache(tex[j],loadflags,blendflags,1-j)
				EndIf
			EndIf
		Next
		
		If isAlpha=1 Then
			If tex[1]<>0 Then
				TextureBlend tex[1],2
				BrushTexture brush,tex[1],0,0
			Else
				BrushTexture brush,blankTexture,0,0
			EndIf
		Else
			
			
			For j=0 To 1
				If tex[j]<>0 Then
					BrushTexture brush,tex[j],0,j
				Else
					BrushTexture brush,blankTexture,0,j
				EndIf
			Next
			
		EndIf
		
		surf=CreateSurface(childMesh)
		
		If isAlpha>0 Then PaintSurface surf,brush
		
		FreeBrush brush : brush = 0
		
		count2=ReadInt(f) ;vertices
		
		For j%=1 To count2
			;world coords
			x=ReadFloat(f) : y=ReadFloat(f) : z=ReadFloat(f)
			vertex=AddVertex(surf,x,y,z)
			
			;texture coords
			For k%=0 To 1
				u=ReadFloat(f) : v=ReadFloat(f)
				VertexTexCoords surf,vertex,u,v,0.0,k
			Next
			
			;colors
			temp1i=ReadByte(f)
			temp2i=ReadByte(f)
			temp3i=ReadByte(f)
			VertexColor surf,vertex,temp1i,temp2i,temp3i,1.0
		Next
		
		count2=ReadInt(f) ;polys
		For j%=1 To count2
			temp1i = ReadInt(f) : temp2i = ReadInt(f) : temp3i = ReadInt(f)
			AddTriangle(surf,temp1i,temp2i,temp3i)
		Next
		
		If isAlpha=1 Then
			AddMesh childMesh,Alpha
		Else
			AddMesh childMesh,Opaque
		EndIf
		
		FreeEntity childMesh
	Next
	
	Local obj%
	
	If brush <> 0 Then FreeBrush brush
	
	FreeTexture blankTexture
	
	Local rm2% = WriteFile(Replace(origFile,".rmesh",".rm2"))
	WriteByte(rm2,Asc(".")) : WriteByte(rm2,Asc("R")) : WriteByte(rm2,Asc("M")) : WriteByte(rm2,Asc("2"))
	
	WriteByte(rm2,RM2_TEXTURES)
	
	count = 0
	For tc.Materials = Each Materials
		count=count+1
	Next
	WriteByte(rm2,count)
	For tc.Materials = Each Materials
		WriteByteString(rm2,tc\Name)
		WriteByte(rm2,(tc\loadflags Shl 4) Or tc\blendflags)
		WriteByte(rm2,tc\uvSet)
	Next
	
	For i% = 1 To CountSurfaces(Opaque)
		surf% = GetSurface(Opaque,i)
		
		If CountVertices(surf)>0 Then
			WriteByte(rm2,RM2_OPAQUE)
			
			brush% = GetSurfaceBrush(surf)
			For j=0 To 1
				tx% = GetBrushTexture(brush,j)
				
				If tx<>0 Then
					stri$ = StripPath(TextureName(tx))				
					found% = False
					count% = 0
					For tc.Materials = Each Materials
						count=count+1
						If tc\Name = StripPath(TextureName(tx)) Then
							found = True
							;DebugLog j+" "+tc\Name+" "+count
							Exit
						EndIf
					Next
					If found Then
						WriteByte(rm2,count)
					Else
						WriteByte(rm2,0)
					EndIf
					
					FreeTexture tx
				Else
					WriteByte(rm2,0)
				EndIf
			Next
			FreeBrush brush
			
			;DebugLog CountVertices(surf)
			WriteShort(rm2,CountVertices(surf))
			For j% = 1 To CountVertices(surf)
				WriteFloat(rm2,VertexX(surf,j-1))
				WriteFloat(rm2,VertexY(surf,j-1))
				WriteFloat(rm2,VertexZ(surf,j-1))
				
				WriteByte(rm2,VertexRed(surf,j-1))
				WriteByte(rm2,VertexGreen(surf,j-1))
				WriteByte(rm2,VertexBlue(surf,j-1))
				
				For k% = 0 To 1
					WriteFloat(rm2,VertexU(surf,j-1,k))
					WriteFloat(rm2,VertexV(surf,j-1,k))
				Next
			Next
			
			WriteShort(rm2,CountTriangles(surf))
			For j% = 1 To CountTriangles(surf)
				WriteShort(rm2,TriangleVertex(surf,j-1,0))
				WriteShort(rm2,TriangleVertex(surf,j-1,1))
				WriteShort(rm2,TriangleVertex(surf,j-1,2))
			Next
		EndIf
	Next
	
	For i% = 1 To CountSurfaces(Alpha)
		surf% = GetSurface(Alpha,i)
		
		If CountVertices(surf)>0 Then
			WriteByte(rm2,RM2_ALPHA)
			
			brush% = GetSurfaceBrush(surf)
			For j=0 To 1
				tx% = GetBrushTexture(brush,j)
				
				If tx<>0 Then
					stri$ = StripPath(TextureName(tx))				
					found% = False
					count% = 0
					For tc.Materials = Each Materials
						count=count+1
						If tc\Name = StripPath(TextureName(tx)) Then
							found = True
							;DebugLog j+" "+tc\Name+" "+count
							Exit
						EndIf
					Next
					If found Then
						WriteByte(rm2,count)
					Else
						WriteByte(rm2,0)
					EndIf
					
					FreeTexture tx
				Else
					WriteByte(rm2,0)
				EndIf
			Next
			FreeBrush brush
			
			;DebugLog CountVertices(surf)
			WriteShort(rm2,CountVertices(surf))
			For j% = 1 To CountVertices(surf)
				WriteFloat(rm2,VertexX(surf,j-1))
				WriteFloat(rm2,VertexY(surf,j-1))
				WriteFloat(rm2,VertexZ(surf,j-1))
				
				WriteByte(rm2,VertexRed(surf,j-1))
				WriteByte(rm2,VertexGreen(surf,j-1))
				WriteByte(rm2,VertexBlue(surf,j-1))
				
				For k% = 0 To 1
					WriteFloat(rm2,VertexU(surf,j-1,k))
					WriteFloat(rm2,VertexV(surf,j-1,k))
				Next
			Next
			
			WriteShort(rm2,CountTriangles(surf))
			For j% = 1 To CountTriangles(surf)
				WriteShort(rm2,TriangleVertex(surf,j-1,0))
				WriteShort(rm2,TriangleVertex(surf,j-1,1))
				WriteShort(rm2,TriangleVertex(surf,j-1,2))
			Next
		EndIf
	Next
	
	Local hiddenMesh%
	
	Local totalVerts% = 0
	Local totalTris% = 0
	
	count=ReadInt(f) ;invisible collision mesh
	If count>0 Then
		hiddenMesh=CreateMesh()
		surf=CreateSurface(hiddenMesh)
		For i%=1 To count
			count2=ReadInt(f) ;vertices
			totalVerts=totalVerts+count2
			For j%=1 To count2
				;world coords
				x=ReadFloat(f) : y=ReadFloat(f) : z=ReadFloat(f)
				vertex=AddVertex(surf,x,y,z)
			Next
			
			count2=ReadInt(f) ;polys
			totalTris=totalTris+count2
			For j%=1 To count2
				temp1i = ReadInt(f) : temp2i = ReadInt(f) : temp3i = ReadInt(f)
				AddTriangle(surf,totalVerts+temp1i,totalVerts+temp2i,totalVerts+temp3i)
			Next
		Next
		
		WriteByte(rm2,RM2_INVISIBLE)
		WriteShort(rm2,CountVertices(surf))
		For i% = 1 To CountVertices(surf)
			WriteFloat(rm2,VertexX(surf,i-1))
			WriteFloat(rm2,VertexY(surf,i-1))
			WriteFloat(rm2,VertexZ(surf,i-1))
		Next
		
		WriteShort(rm2,CountTriangles(surf))
		For i% = 1 To CountTriangles(surf)
			WriteShort(rm2,TriangleVertex(surf,i-1,0))
			WriteShort(rm2,TriangleVertex(surf,i-1,1))
			WriteShort(rm2,TriangleVertex(surf,i-1,2))
		Next
		FreeEntity hiddenMesh
	EndIf
	
	EntityPickMode Opaque,2,True
	EntityPickMode Alpha,2,True
	
	Local count3%
	
	If hasTriggerBox Then ;skip this triggerbox bullshit
		If Instr(origFile,"closet")>0 Then
			DebugLog "HAS TRIGGERBOX"
		EndIf
		count = ReadInt(f)
		For tb = 0 To count-1
			count2 = ReadInt(f)
			For i%=1 To count2
				count3=ReadInt(f)
				For j%=1 To count3
					x=ReadFloat(f) : y=ReadFloat(f) : z=ReadFloat(f)
				Next
				count3=ReadInt(f)
				For j%=1 To count3
					temp1i = ReadInt(f) : temp2i = ReadInt(f) : temp3i = ReadInt(f)
				Next
			Next
			ReadString(f)
		Next
	EndIf
	
	count=ReadInt(f) ;point entities
	For i%=1 To count
		temp1s=ReadString(f)
		If Instr(origFile,"closet")>0 Then
			DebugLog temp1s
		EndIf
		Select temp1s
			Case "screen"
				WriteByte(rm2,RM2_SCREEN)
				
				WriteFloat(rm2,ReadFloat(f))
				WriteFloat(rm2,ReadFloat(f))
				WriteFloat(rm2,ReadFloat(f))
				
				WriteByteString(rm2,ReadString(f))
				
			Case "waypoint"
				;DON'T WRITE YET
				x = ReadFloat(f)
				y = ReadFloat(f)
				z = ReadFloat(f)
				
				CreateWaypoint(x,y,z)
			Case "light"
				WriteByte(rm2,RM2_POINTLIGHT)
				
				WriteFloat(rm2,ReadFloat(f)) ;x
				WriteFloat(rm2,ReadFloat(f)) ;y
				WriteFloat(rm2,ReadFloat(f)) ;z
				
				WriteFloat(rm2,ReadFloat(f)) ;range
				
				lcolor$=ReadString(f)
				r%=Int(Piece(lcolor,1," "))
				g%=Int(Piece(lcolor,2," "))
				b%=Int(Piece(lcolor,3," "))
				WriteByte(rm2,r) : WriteByte(rm2,g) : WriteByte(rm2,b)
				
				WriteByte(rm2,Min(ReadFloat(f),1.0)*255)
			Case "spotlight"
				WriteByte(rm2,RM2_SPOTLIGHT)
				
				WriteFloat(rm2,ReadFloat(f)) ;x
				WriteFloat(rm2,ReadFloat(f)) ;y
				WriteFloat(rm2,ReadFloat(f)) ;z
				
				WriteFloat(rm2,ReadFloat(f)) ;range
				
				lcolor$=ReadString(f)
				r%=Int(Piece(lcolor,1," "))
				g%=Int(Piece(lcolor,2," "))
				b%=Int(Piece(lcolor,3," "))
				WriteByte(rm2,r) : WriteByte(rm2,g) : WriteByte(rm2,b)
				
				WriteByte(rm2,Min(ReadFloat(f),1.0)*255)
				
				angles$=ReadString(f)
				WriteFloat(rm2,Piece(angles,1," ")) ;pitch
				WriteFloat(rm2,Piece(angles,2," ")) ;yaw
				
				WriteFloat(rm2,ReadInt(f)) ;innerconeangle
				WriteFloat(rm2,ReadInt(f)) ;outerconeangle
			Case "soundemitter"
				WriteByte(rm2,RM2_SOUNDEMITTER)
				
				WriteFloat(rm2,ReadFloat(f))
				WriteFloat(rm2,ReadFloat(f))
				WriteFloat(rm2,ReadFloat(f))
				WriteByte(rm2,ReadInt(f))
				WriteFloat(rm2,ReadFloat(f))
			Case "model"
				propfile$ = ReadString(f)
				If propfile<>"" Then
					WriteByte(rm2,RM2_PROP)
					
					WriteByteString(rm2,propfile)
					
					temp1=ReadFloat(f) : temp2=ReadFloat(f) : temp3=ReadFloat(f) ;position
					WriteFloat(rm2,temp1) : WriteFloat(rm2,temp2) : WriteFloat(rm2,temp3)
					
					temp1=ReadFloat(f) : temp2=ReadFloat(f) : temp3=ReadFloat(f) ;rotation
					WriteFloat(rm2,temp1) : WriteFloat(rm2,temp2) : WriteFloat(rm2,temp3)
					
					temp1=ReadFloat(f) : temp2=ReadFloat(f) : temp3=ReadFloat(f) ;scale
					WriteFloat(rm2,temp1) : WriteFloat(rm2,temp2) : WriteFloat(rm2,temp3)
				Else
					RuntimeError "error"
				EndIf
		End Select
	Next
	
	Select Replace(StripPath(origFile),".rmesh","")
		Case "chck_hcz_ez_2"
			CreateWaypoint(-48.0,50.0+0,128.0)
			CreateWaypoint(352.0,50.0+0,128.0)
			CreateWaypoint(0,50.0+0,-4.0)
		Case "chck_lcz_hcz_2"
			CreateWaypoint(48.0,50.0+0,-128.0)
			CreateWaypoint(-352.0,50.0+0,-128.0)
			CreateWaypoint(0,50.0+0,-4.0)
		Case "closets_2"
			CreateWaypoint(-240.0,50.0+0.0,0)
		Case "cont_008_1"
			CreateWaypoint(296.0,50.0+0,-672.0)
			CreateWaypoint(296.0,50.0+0,-144.0)
			CreateWaypoint(-384.0,50.0+0,-672.0)
		Case "cont_012_2"
			CreateWaypoint(264.0,50.0+0.0,672.0)
			CreateWaypoint(-512.0,50.0-768.0,-336.0)
		Case "cont_035_1"
			CreateWaypoint(-296.0,50.0+0,-672.0)
			CreateWaypoint(-296.0,50.0+0,-144.0)
			CreateWaypoint(384.0,50.0+0,-672.0)
			CreateWaypoint(768.0,50.0+0,512.0)
		Case "cont_049_2"
			CreateWaypoint(328.0,50.0+0.0,656.0)
			CreateWaypoint(-2328.0,50.0-3520.0,-656.0)
			CreateWaypoint(-328.0,50.0+0.0,-656.0)
			CreateWaypoint(2360.0,50.0-3520.0,656.0)
			CreateWaypoint(272.0,50.0-3552.0,104.0)
			CreateWaypoint(0,50.0+0,0)
		Case "cont_079_1"
			CreateWaypoint(0,50.0-448.0,1136.0)
			CreateWaypoint(1456.0,50.0-448.0,976.0)
			CreateWaypoint(1144.0,50.0-448.0,704.0)
		Case "cont_106_1"
			CreateWaypoint(-968.0,50.0-764.0,1392.0)
			CreateWaypoint(0,50.0+0,-464.0)
			CreateWaypoint(-624.0,50.0-1280.0,0)
		Case "cont_1123_2"
			CreateWaypoint(832.0,50.0+0.0,367.0)
			CreateWaypoint(280.0,50.0+0.0,-607.0)
			CreateWaypoint(280.0,50.0+512.0,-607.0)
		Case "cont_1162_2c"
			CreateWaypoint(248.0,50.0+0.0,-736.0)
		Case "cont_173_1"
			CreateWaypoint(4000.0,50.0+384.0,1696.0)
			CreateWaypoint(2704.0,50.0+384.0,624.0)
			CreateWaypoint(1392.0,50.0+384.0,64.0)
			CreateWaypoint(-640.0,50.0+384.0,64.0)
			CreateWaypoint(1280.0,50.0+384.0,312.0)
			CreateWaypoint(0,50.0+0,1184.0)
		Case "cont_205_1"
			CreateWaypoint(128.0,50.0+0,640.0)
			CreateWaypoint(-1392.0,50.0-128.0,-384)
		Case "cont_500_1499_2"
			CreateWaypoint(288.0,50.0+0,576.0)
			CreateWaypoint(777.0,50.0+0,671.0)
			CreateWaypoint(556.0,50.0+0,296.0)
		Case "cont_513_3"
			CreateWaypoint(-704.0,50.0+0,304.0)
		Case "cont_714_860_1025_2"
			CreateWaypoint(264.0,50.0+0,0)
			CreateWaypoint(-264.0,50.0+0,32)
			CreateWaypoint(-560.0,50.0+0,-240.0)
			CreateWaypoint(560.0,50.0+0,-272.0)
			CreateWaypoint(560.0,50.0+0,272.0)
		Case "cont_895_1"
			CreateWaypoint(0,50.0+0,-448.0)
		Case "cont_914_1"
			CreateWaypoint(0,50.0+0,-368.0)
			CreateWaypoint(0,50.0+0,-368.0)
			CreateWaypoint(-624.0,50.0+0.0,528.0)
			CreateWaypoint(816.0,50.0+0.0,528.0)
		Case "cont_966_3"
			CreateWaypoint(-400.0,50.0+0,0)
			CreateWaypoint(0,50.0+0,-480.0)
		Case "end_1"
			CreateWaypoint(0,50.0+0,1136)
		Case "exit_gatea_1"
			CreateWaypoint(744.0,50.0+0,512.0)
			CreateWaypoint(0,50.0+0,-360.0)
		Case "exit_gateb_1"
			CreateWaypoint(0,50.0+0,-320.0)
			CreateWaypoint(720.0,50.0+0,1432.0)
			CreateWaypoint(-5424.0,50.0+10784.0,-1380.0)
			CreateWaypoint(4352.0,50.0+10784.0,-492.0)
			CreateWaypoint(4352.0,50.0+10784.0,500.0)
			CreateWaypoint(3248.0,50.0+9856.0,6400.0)
			CreateWaypoint(3072.0,50.0+9856.0,5800.0)
		Case "extend_gatea_1"
			CreateWaypoint(-4064.0,50.0+(-1280.0+12000.0),3952.0)
			CreateWaypoint(0,50.0+12000.0,-1024.0)
			CreateWaypoint(-1440,50.0+(12000.0-480.0),2328.0)
			CreateWaypoint(-1440,50.0+(12000.0-480.0),4352.0)
			CreateWaypoint(1544.0,50.0+12000.0,-64.0)
		Case "hll_bench_3"
		Case "hll_caf_2"
		Case "hll_dirty_3"
		Case "hll_ele_2"
			CreateWaypoint(448.0,50.0+0.0,0)
		Case "hll_fan_2"
		Case "hll_gas_2"
		Case "hll_gas_3"
		Case "hll_lights_2"
		Case "hll_lshape_2"
		Case "hll_sl_2"
			CreateWaypoint(480.0,50.0+0,-640.0)
			CreateWaypoint(544.0,50.0+480.0,256.0)
			CreateWaypoint(1504.0,50.0+480.0,960.0)
		Case "hll_toilets_2"
		Case "hll_tsl"
		Case "intro"
			;TODO: get the waypoints right in the new intro
;			CreateWaypoint(288.0,50.0+0,384.0)
;			CreateWaypoint(-1008.0,50.0+0,-688.0)
;			CreateWaypoint(-2320.0,50.0+0,-1248.0)
;			CreateWaypoint(-4352.0,50.0+0,-1248.0)
;			CreateWaypoint(-3712.0,50.0-385,-128.0)
;			CreateWaypoint(-3712,50.0-385,-2336)
;			CreateWaypoint(-6864,50.0+0,-1248)
;			CreateWaypoint(-5856,50.0+0,-1504)
;			CreateWaypoint(-2432,50.0+0,-1000)
;			CreateWaypoint(-5760,50.0+0,(320+896*ztemp))
;			CreateWaypoint(-8288,50.0+0,(320+896*ztemp))
;			CreateWaypoint(-(7424.0-512.0*xtemp),50.0+0,(1008.0-480.0*ztemp))
;			CreateWaypoint(-(5120.0-512.0*xtemp),50.0+0,(1008.0-480.0*ztemp))
		Case "lck_096_2c"
		Case "lck_air_2","lck_air_broke_2"
			CreateWaypoint(336.0,50.0+0.0,-382.0)
			CreateWaypoint(336.0,50.0+0.0,462.0)
		Case "lck_broke_2c"
		Case "lck_cam_2c"
			CreateWaypoint(-736.0,50.0+0,-104.0)
			CreateWaypoint(104.0,50.0+0,736.0)
		Case "lck_ez_3"
			CreateWaypoint(-728.0,50.0+0.0,-458.0)
			CreateWaypoint(-223.0,50.0+0.0,-736.0)
			CreateWaypoint(-459.0,50.0+0.0,339.0)
			CreateWaypoint(385.0,50.0+0.0,339.0)
		Case "lck_tshape_2"
			CreateWaypoint(0,50.0+0,528.0)
			CreateWaypoint(0,50.0+0,-528.0)
		Case "lifts_1"
		Case "off_2level_2"
			CreateWaypoint(-1056.0,50.0+384.0,290.0)
		Case "off_bain_2"
			CreateWaypoint(-240.0,50.0+0.0,0)
		Case "off_gears_may_har_2"
			CreateWaypoint(240.0,50.0+0.0,448.0)
			CreateWaypoint(-496.0,50.0+0.0,0)
			CreateWaypoint(240.0,50.0+0.0,-576.0)
		Case "off_glss_3"
			CreateWaypoint(736.0,50.0+0.0,240.0)
		Case "off_lower_2"
		Case "off_l_conf_2"
			CreateWaypoint(240.0,50.0+0.0,48.0)
			CreateWaypoint(-432.0,50.0+0.0,0)
		Case "off_plain_2"
		Case "off_rosewood_2"
			CreateWaypoint(1440.0,50.0+224.0,32.0)
		Case "pocketdimension"
			CreateWaypoint(0,50.0+2048,32.0-1024)
			CreateWaypoint(0,50.0+2048,32.0+1024)
		Case "scp_970_2"
			CreateWaypoint(-1288.0,50.0+0,0)
			CreateWaypoint(-760.0,50.0+0,0)
			CreateWaypoint(-264.0,50.0+0,0)
			CreateWaypoint(264.0,50.0+0,0)
			CreateWaypoint(760.0,50.0+0,0)
			CreateWaypoint(1288.0,50.0+0,0)
		Case "srvr_096_2"
			CreateWaypoint(0,50.0+0,0)
			CreateWaypoint(-208.0,50.0+0.0,-736.0)
			CreateWaypoint(-208.0,50.0+0.0,736.0)
			CreateWaypoint(-672.0,50.0+0.0,-1024.0)
		Case "srvr_farm_3"
		Case "srvr_lshape_3"
		Case "srvr_pc_2"
			CreateWaypoint(264.0,50.0+0.0,672.0)
			CreateWaypoint(-512.0,50.0-768.0,-336.0)
			CreateWaypoint(-509.0,50.0-768.0,-1037.0)
		Case "strg_939_2"
			CreateWaypoint(0,50.0+0.0,448.0)
			CreateWaypoint(608.0,50.0+0.0,-312.0)
		Case "strg_elec_2c"
			CreateWaypoint(64.0,50.0+0.0,368.0)
		Case "test_682_2"
			CreateWaypoint(720.0,50.0+0,0)
			CreateWaypoint(-624.0,50.0-1280.0,0)
		Case "test_860_2"
			CreateWaypoint(928.0,50.0+0,640.0)
			CreateWaypoint(928.0,50.0+0,-640.0)
			CreateWaypoint(416.0,50.0+0,-640.0)
			CreateWaypoint(416.0,50.0+0,640.0)
		Case "test_smallwindow_2"
			CreateWaypoint(-240.0,50.0+0.0,640.0)
			CreateWaypoint(-240.0,50.0+0.0,640.0)
			CreateWaypoint(-512.0,50.0+0.0,384.0)
			CreateWaypoint(-816.0,50.0+0.0,-208.0)
		Case "tnnl_elec_2"
		Case "tnnl_maintenance_2"
			CreateWaypoint(264.0,50.0+0.0,656.0)
			CreateWaypoint(-264.0,50.0+0.0,-656.0)
			CreateWaypoint(0,50.0+0,0)
		Case "tnnl_nuke_2"
			CreateWaypoint(576.0,50.0+0.0,-152.0)
			CreateWaypoint(-544.0,50.0+1504.0,738.0)
			CreateWaypoint(1192.0,50.0+0.0,0)
			CreateWaypoint(680.0,50.0+1504.0,0)
		Case "tnnl_pipes_2"
		Case "tnnl_plain_3"
	End Select
	
	a% = 0
	For wpt.WaypointTemp = Each WaypointTemp
		a=a+1
		i% = 0
		c% = 0
		wpt\connectedTo[0]=0
		For wpt2.WaypointTemp = Each WaypointTemp
			c=c+1
			If wpt2<>wpt Then
				If EntityVisibleLazy(wpt\pivot,wpt2\pivot) And EntityVisibleLazy(wpt2\pivot,wpt\pivot) Then
					i=i+1
					If a>c Then
						ConnectWaypoints(a,wpt,c,wpt2)
					EndIf
				EndIf
			EndIf
		Next
		If i=0 Then
			If c>1 Then
				DebugLog a+" orphaned: removing"
				a=a-1
				FreeEntity wpt\pivot
				Delete wpt
			Else
				;DebugLog a+" LONE WOLF"
			EndIf
		EndIf
	Next
	
	a% = 0
	For wpt.WaypointTemp = Each WaypointTemp
		a=a+1
		If Instr(origFile,"closet")>0 Then
			DebugLog "WAYPOINT "+a
		EndIf
		WriteByte(rm2,RM2_WAYPOINT)
		WriteFloat(rm2,wpt\x)
		WriteFloat(rm2,wpt\y)
		WriteFloat(rm2,wpt\z)
		For i% = 0 To 32
			WriteByte(rm2,wpt\connectedTo[i])
			If wpt\connectedTo[i]=0 Then Exit
		Next
	Next
	
	CloseFile f
	CloseFile rm2
	
	EntityParent Alpha,Opaque
	Return Opaque
End Function

Function ConnectWaypoints(i1%,wp1.WaypointTemp,i2%,wp2.WaypointTemp)
	For i% = 0 To 15
		If wp1\connectedTo[i]=0 Then
			wp1\connectedTo[i]=i2
			wp1\connectedTo[i+1]=0
			Exit
		EndIf
	Next
	
	For i% = 0 To 15
		If wp2\connectedTo[i]=0 Then
			wp2\connectedTo[i]=i1
			wp2\connectedTo[i+1]=0
			Exit
		EndIf
	Next
End Function

Function LoadRoomTemplates(file$)
	r% = ReadFile(file)
	While Not Eof(r)
		ln$ = ReadLine(r)
		If (Left(ln,1)="[") And (Instr(ln,"room amb")=0) Then
			ln = Mid(ln, 2, Len(ln) - 2)
			If Instr(GetINIString(file,ln,"zones"),"lcz")>0 Or True Then
				meshpath$ = GetINIString(file,ln,"meshpath")
				DebugLog "CONVERTING: "+meshpath
				mesh% = LoadRMesh(meshpath)
				
				For wpt.WaypointTemp = Each WaypointTemp
					FreeEntity wpt\pivot
					Delete wpt
				Next
				
				FreeEntity mesh
				
				ClearTextureCache
			EndIf
		EndIf
	Wend
End Function

Graphics3D 640,480,0,2
cam% = CreateCamera()
PositionEntity cam,0.0,500.0,0.0
RotateEntity cam,45,0,0
CameraRange cam,1.0,5000.0

LoadRoomTemplates("Data/rooms.ini")

;~IDEal Editor Parameters:
;~F#1B#2A#3A#4B#61#68#7E#86#8E#96
;~C#Blitz3D