Function Min#(a#,b#)
	If (a<b) Then Return a
	Return b
End Function

Function Max#(a#,b#)
	If (a>b) Then Return a
	Return b
End Function

Include "../SourceCode/INI.bb"

;RMESH STUFF;;;;

Type Material
	Field name$
	Field loadflags%
	Field blendflags%
	Field uvSet%
	Field diff%

	Field StepSound%
End Type

Function StripFilename$(file$)
	Local mi$=""
	Local lastSlash%=0
	Local i%
	If (Len(file)>0) Then
		For i=1 To Len(file)
			mi=Mid(file,i,1)
			If (mi="\" Or mi="/") Then
				lastSlash=i
			EndIf
		Next
	EndIf

	Return Left(file,lastSlash)
End Function

Function StripPath$(file$)
	Local name$=""
	Local i%, mi$
	If (Len(file)>0) Then
		For i=Len(file) To 1 Step -1

			mi = Mid$(file,i,1)
			If (mi="\" Or mi="/") Then Return name

			name=mi+name
		Next

	EndIf

	Return name
End Function

Function Piece$(s$,entry%,char$=" ")
	Local a$, p%

	While Instr(s,char+char)
		s=Replace(s,char+char,char)
	Wend
	Local n%
	For n=1 To entry-1
		p = Instr(s,char)
		s=Right(s,Len(s) - p)
	Next
	p=Instr(s,char)
	If (p<1) Then
		a=s
	Else
		a=Left(s,p-1)
	EndIf

	Return a
End Function

Function KeyValue$(entity%,key$,defaultvalue$="")
	Local test$, testkey$, value$
	Local properties$ = EntityName(entity)
	Local p%

	properties=Replace(properties,Chr(13),"")
	key=Lower(key)
	Repeat
		p = Instr(properties,Chr(10))
		If (p) Then
			test=(Left(properties,p-1))
		Else
			test=properties
		EndIf
		testkey=Piece(test,1,"=")
		testkey=Trim(testkey)
		testkey=Replace(testkey,Chr(34),"")
		testkey=Lower(testkey)
		If (testkey=key) Then
			value=Piece(test,2,"=")
			value=Trim(value)
			value=Replace(value,Chr(34),"")
			Return value
		EndIf
		If (Not p) Then Return defaultvalue
		properties=Right(properties,Len(properties)-p)
	Forever
End Function


Function GetTextureFromCache%(name$)
	Local tc.Material
	For tc=Each Material
		If (tc\name = name) Then Return tc\diff
	Next
	Return 0
End Function

Function GetCache.Material(name$)
	Local tc.Material
	For tc=Each Material
		If (tc\name = name) Then Return tc
	Next
	Return Null
End Function

Function AddTextureToCache(texture%,loadflags%,blendflags%,uvSet%)
	If (Trim(StripPath(TextureName(texture)))="") Then Return
	Local tc.Material=GetCache(StripPath(TextureName(texture)))
	;DebugLog(StripPath(TextureName(texture)))
	If (tc=Null) Then
		tc=New Material
		tc\name=StripPath(TextureName(texture))
		tc\loadflags=loadflags
		tc\blendflags=blendflags
		tc\uvSet=uvSet
		tc\diff=0
	EndIf
	If (tc\diff=0) Then tc\diff=texture
End Function

Function ClearTextureCache()
	Local tc.Material
	For tc=Each Material
		If (tc\diff<>0) Then FreeTexture(tc\diff)
		;If tc\bump<>0 Then FreeTexture(tc\bump)
		Delete tc
	Next
End Function

Function FreeTextureCache()
	Local tc.Material
	For tc=Each Material
		If (tc\diff<>0) Then FreeTexture(tc\diff)
		;If tc\bump<>0 Then FreeTexture(tc\bump)
		tc\diff = 0; : tc\bump = 0
	Next
End Function

Function LoadRMeshTexture%(roompath$,name$,flags%)
	If (FileType(roompath+name)=1) Then
		Return LoadTexture(roompath+name,flags)
	Else
		Return LoadTexture("GFX/Map/"+name,flags) ;TODO: don't hardcode?
	EndIf
End Function

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
	Local i%
	For i = 1 To Len(stri)
		WriteByte(stream,Asc(Mid(stri,i,1)))
	Next
End Function

Function ReadByteString$(stream%)
	Local length% = ReadByte(stream)
	Local retVal$ = ""
	Local i%
	For i = 1 To length
		retVal=retVal+Chr(ReadByte(stream))
	Next
	Return retVal
End Function

Function Fix(filename$)
	Local rmeshFile$ = Replace(Replace(filename,"GFX/Map/Rooms/","angery/"),".rm2",".rmesh")

	Local f%=ReadFile(rmeshFile)
	Local i%,j%,k%,x#,y#,z#,yaw#
	Local vertex%
	Local temp1i%,temp2i%,temp3i%
	Local temp1#,temp2#,temp3#
	Local temp1s$, temp2s$

	Local hasTriggerBox% = False

	For i=0 To 3 ;reattempt up to 3 times
		If (f=0) Then
			f=ReadFile(rmeshFile)
		Else
			Exit
		EndIf
	Next
	If (f=0) Then RuntimeError("Error reading file "+Chr(34)+rmeshFile+Chr(34))
	Local isRMesh$ = ReadString(f)
	If (isRMesh="RoomMesh") Then
		;Continue
	ElseIf ((isRMesh="RoomMesh.HasTriggerBox")) Then
		hasTriggerBox = True
	Else
		RuntimeError(Chr(34)+rmeshFile+Chr(34)+" is Not RMESH ("+isRMesh+")")
	EndIf

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
		For j=0 To 1
			temp1i=ReadByte(f)
			If (temp1i<>0) Then
				temp1s=ReadString(f)
			EndIf
		Next

		count2=ReadInt(f) ;vertices

		For j=1 To count2
			;world coords
			x=ReadFloat(f) : y=ReadFloat(f) : z=ReadFloat(f)

			;texture coords
			For k=0 To 1
				u=ReadFloat(f) : v=ReadFloat(f)
			Next

			;colors
			temp1i=ReadByte(f)
			temp2i=ReadByte(f)
			temp3i=ReadByte(f)
		Next

		count2=ReadInt(f) ;polys
		For j=1 To count2
			temp1i = ReadInt(f) : temp2i = ReadInt(f) : temp3i = ReadInt(f)
		Next

	Next

	Local hiddenMesh% = 0
	Local hbMesh% = 0
	Local hbSurf% = 0

	Local totalVerts% = 0
	Local totalTris% = 0

	Select Replace(StripPath(rmeshFile),".rmesh","")
		Case "cont_049_2"
			hbMesh = LoadMesh("angery/room049_hb.b3d")
		Case "cont_1123_2"
			hbMesh = LoadMesh("angery/1123_hb.b3d")
		Case "hll_dirty_3"
			hbMesh = LoadMesh("angery/room3z2_hb.b3d")
		Case "hll_sl_2"
			hbMesh = LoadMesh("angery/room2sl_hb.b3d")
		Case "off_glss_3"
			hbMesh = LoadMesh("angery/room3offices_hb.b3d")
		Case "strg_939_2"
			hbMesh = LoadMesh("angery/room3storage_hb.b3d")
	End Select

	If (hbMesh<>0) Then
		hiddenMesh = CreateMesh()
		surf = CreateSurface(hiddenMesh)

		For i = 1 To CountSurfaces(hbMesh)
			hbSurf = GetSurface(hbMesh,i)
			For j = 0 To CountVertices(hbSurf)-1
				AddVertex(surf,VertexX(hbSurf,j),VertexY(hbSurf,j),VertexZ(hbSurf,j))
			Next

			For j = 0 To CountTriangles(hbSurf)-1
				AddTriangle(surf,TriangleVertex(hbSurf,j,0)+totalVerts,TriangleVertex(hbSurf,j,1)+totalVerts,TriangleVertex(hbSurf,j,2)+totalVerts)
			Next

			totalVerts=totalVerts+CountVertices(hbSurf)
			totalTris=totalTris+CountTriangles(hbSurf)
		Next
		FreeEntity(hbMesh)
	EndIf

	Local surfVertCount% = 0
	count=ReadInt(f) ;invisible collision mesh
	If (count>0) Then
		If (hiddenMesh=0) Then
			hiddenMesh=CreateMesh()
			surf=CreateSurface(hiddenMesh)
		Else
			surf=GetSurface(hiddenMesh,1)
		EndIf
		For i=1 To count
			count2=ReadInt(f) ;vertices
			surfVertCount = count2

			For j=1 To count2
				;world coords
				x=ReadFloat(f) : y=ReadFloat(f) : z=ReadFloat(f)
				vertex=AddVertex(surf,x,y,z)
			Next

			count2=ReadInt(f) ;polys

			For j=1 To count2
				temp1i = ReadInt(f) : temp2i = ReadInt(f) : temp3i = ReadInt(f)
				AddTriangle(surf,totalVerts+temp1i,totalVerts+temp2i,totalVerts+temp3i)
			Next
			totalVerts=totalVerts+surfVertCount
			totalTris=totalTris+count2
		Next
	EndIf
	CloseFile(f)

	Local rm2File$ = filename

	Local file% = ReadFile(rm2File)
	Local fixedFile% = WriteFile(rm2File+"_boyo")

	If (file=0) Then
		RuntimeError("Failed to read "+rm2File)
	EndIf
	If (fixedFile=0) Then RuntimeError("AAA")

	Local cuboid%

	Local header$ = ""
	For i = 0 To 3
		cuboid = ReadByte(file)
		header=header+Chr(cuboid)
		WriteByte(fixedFile,cuboid)
	Next

	If (header<>".RM2") Then
		RuntimeError("Error while loading "+rm2File+": expected .RM2, found "+header)
	EndIf

	Local partType%

	Local texName$
	Local flags%
	Local loadFlags%
	Local blendFlags%
	Local uvSet%
	Local texture%
	Local shouldLoadTexture%

	Local mesh%
	Local clonedMesh%
	Local textureIndex%[2]
	Local layerCount%
	Local mat.Material
	Local vert1%
	Local vert2%
	Local vert3%

	Local range#
	Local intensity#

	Local pitch#
	Local roll#

	Local innerConeAngle#
	Local outerConeAngle#


	Local ambienceInd%

	Local propName$
	Local xScale#
	Local yScale#
	Local zScale#

	Local r%,g%,b%

	Local prevType%
	While Not Eof(file)
		prevType = partType
		partType = ReadByte(file)
		If (partType<>RM2_INVISIBLE) Then WriteByte(fixedFile,partType)
		Select partType
			Case RM2_TEXTURES
				;[Block]
				count = ReadByte(file)
				WriteByte(fixedFile,count)
				For i = 0 To count-1
					texName = ReadByteString(file)
					WriteByteString(fixedFile,texName)
					flags = ReadByte(file)
					WriteByte(fixedFile,flags)
					loadFlags = flags Shr 4
					blendFlags = flags And $0F
					uvSet = ReadByte(file)
					WriteByte(fixedFile,uvSet)
				Next
				;[End Block]
			Case RM2_OPAQUE,RM2_ALPHA
				;[Block]
				layerCount = 0
				For i = 0 To 1
					textureIndex[i] = ReadByte(file)
					WriteByte(fixedFile,textureIndex[i])
					If (textureIndex[i]>0) Then layerCount=layerCount+1
				Next

				;vertices
				count = ReadShort(file)
				WriteShort(fixedFile,count)
				For i=0 To count-1
					x = ReadFloat(file) : y = ReadFloat(file) : z = ReadFloat(file)
					WriteFloat(fixedFile,x) : WriteFloat(fixedFile,y) : WriteFloat(fixedFile,z)

					r = ReadByte(file) : g = ReadByte(file) : b = ReadByte(file)
					WriteByte(fixedFile,r) : WriteByte(fixedFile,g) : WriteByte(fixedFile,b)

					For j=0 To 1
						u = ReadFloat(file) : v = ReadFloat(file)
						WriteFloat(fixedFile,u) : WriteFloat(fixedFile,v)
					Next
				Next

				;triangles
				count = ReadShort(file)
				WriteShort(fixedFile,count)
				For i=0 To count-1
					vert1 = ReadShort(file) : WriteShort(fixedFile,vert1)
					vert2 = ReadShort(file) : WriteShort(fixedFile,vert2)
					vert3 = ReadShort(file) : WriteShort(fixedFile,vert3)
				Next
				;[End Block]
			Case RM2_INVISIBLE
				;[Block]
				;vertices
				count = ReadShort(file)

				For i=0 To count-1
					x = ReadFloat(file) : y = ReadFloat(file) : z = ReadFloat(file)
				Next

				;triangles
				count = ReadShort(file)

				For i=0 To count-1
					vert1 = ReadShort(file)
					vert2 = ReadShort(file)
					vert3 = ReadShort(file)
				Next
				;[End Block]
			Case RM2_SCREEN
				;[Block]
				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))
				WriteByteString(fixedFile,ReadByteString(file))
				;[End Block]
			Case RM2_WAYPOINT
				;[Block]
				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))
				For i = 0 To 16
					cuboid = ReadByte(file)
					WriteByte(fixedFile,cuboid)
					If (cuboid=0) Then Exit
				Next
				;[End Block]
			Case RM2_POINTLIGHT
				;[Block]
				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))

				WriteFloat(fixedFile,ReadFloat(file))

				WriteByte(fixedFile,ReadByte(file))
				WriteByte(fixedFile,ReadByte(file))
				WriteByte(fixedFile,ReadByte(file))
				WriteByte(fixedFile,ReadByte(file))
				;[End Block]
			Case RM2_SPOTLIGHT
				;[Block]
				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))

				WriteFloat(fixedFile,ReadFloat(file))

				WriteByte(fixedFile,ReadByte(file))
				WriteByte(fixedFile,ReadByte(file))
				WriteByte(fixedFile,ReadByte(file))
				WriteByte(fixedFile,ReadByte(file))

				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))

				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))
				;[End Block]
			Case RM2_SOUNDEMITTER
				;[Block]
				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))

				WriteByte(fixedFile,ReadByte(file))

				WriteFloat(fixedFile,ReadFloat(file))
				;[End Block]
			Case RM2_PROP
				;[Block]
				WriteByteString(fixedFile,ReadByteString(file))

				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))

				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))

				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))
				WriteFloat(fixedFile,ReadFloat(file))
				;[End Block]
			Default
				RuntimeError("Error after reading type "+Str(prevType))
		End Select
	Wend

	If (hiddenMesh<>0) Then
		WriteByte(fixedFile,RM2_INVISIBLE)
		surf = GetSurface(hiddenMesh,1)
		WriteShort(fixedFile,CountVertices(surf))
		For i = 1 To CountVertices(surf)
			WriteFloat(fixedFile,VertexX(surf,i-1))
			WriteFloat(fixedFile,VertexY(surf,i-1))
			WriteFloat(fixedFile,VertexZ(surf,i-1))
		Next

		WriteShort(fixedFile,CountTriangles(surf))
		For i = 1 To CountTriangles(surf)
			WriteShort(fixedFile,TriangleVertex(surf,i-1,0))
			WriteShort(fixedFile,TriangleVertex(surf,i-1,1))
			WriteShort(fixedFile,TriangleVertex(surf,i-1,2))
		Next
		FreeEntity(hiddenMesh)
	EndIf

	CloseFile(fixedFile)
End Function

Function LoadRoomTemplates(file$)
	Local r%,ln$,meshpath$,mesh%
	r = ReadFile(file)
	While Not Eof(r)
		ln = ReadLine(r)
		If ((Left(ln,1)="[") And (Instr(ln,"room amb")=0)) Then
			ln = Mid(ln, 2, Len(ln) - 2)
			If (Instr(ln,"gateb")=0) Then
				If (Instr(GetINIString(file,ln,"zones"),"lcz")>0 Or True) Then
					meshpath = GetINIString(file,ln,"meshpath")
					DebugLog("CONVERTING: "+meshpath)
					Fix(meshpath)
					;mesh% = LoadRMesh(meshpath)

					;For wpt.WayPointTemp = Each WaypointTemp
					;	FreeEntity(wpt\pivot)
					;	Delete wpt
					;Next

					;FreeEntity(mesh)
					;DeleteFile(meshpath)

					ClearTextureCache()
				EndIf
			EndIf
		EndIf
	Wend
End Function

Graphics3D(1280,720,0,2)
ChangeDir("..")
LoadRoomTemplates("Data/rooms.ini")

;~IDEal Editor Parameters:
;~C#Blitz3D