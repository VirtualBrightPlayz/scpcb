
Function Min#(a#,b#)
	If a<b Then Return a
	Return b
End Function

Function Max#(a#,b#)
	If a>b Then Return a
	Return b
End Function

Include "../INI.bb"


;this should be the ONLY header, do NOT make another for new features
Const RM2_HEADER$ = ".RM2"

;should you need any extra features, add a constant to this list
;and add a case to the select in LoadRM2
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

;note: these flags do not necessarily match the options Blitz3D provides for loading textures
Const RM2_LOADFLAG_COLOR% = 1
Const RM2_LOADFLAG_ALPHA% = 2

;note: these flags do not necessarily match the options Blitz3D provides for blending textures
Const RM2_BLENDFLAG_NORMAL% = 0
Const RM2_BLENDFLAG_DIFFUSE% = 1
Const RM2_BLENDFLAG_LM% = 2

Function TrimExtension$(s$)
	For i = Len(s) To 1 Step -1
		If Mid(s,i,1)="." Then
			Return Left(s,i-1)
		EndIf
	Next
	Return s
End Function

Function ReadByteString$(stream%)
	Local retVal$ = ""
	Local length% = ReadByte(stream)
	For i = 1 To length
		retVal=retVal+Chr(ReadByte(stream))
	Next
	Return retVal
End Function

Function WriteByteString(stream%,stri$)
	WriteByte(stream,Len(stri))
	For i = 1 To Len(stri)
		WriteByte(stream,Asc(Mid(stri,i,1)))
	Next
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

Function StripFilename$(file$)
	Local mi$=""
	Local lastSlash%=0
	If Len(file)>0
		For i=1 To Len(file)
			mi=Mid(file$,i,1)
			If mi="\" Or mi="/" Then
				lastSlash=i
			EndIf
		Next
	EndIf
	
	Return Left(file,lastSlash)
End Function

Function FixRM2Props(fullFilename$)
	Local filename$ = StripPath(fullFilename)
	Local filepath$ = StripFilename(fullFilename)
	
	Local file% = ReadFile(fullFilename)
	Local writer% = WriteFile("temp.rm2")
	
	
	If file=0 Then
		RuntimeError("Failed to read "+fullFilename)
	EndIf
	
	Local byte% = 0
	Local header$ = ""
	For i = 0 To 3
		byte = ReadByte(file) : WriteByte(writer,byte)
		header=header+Chr(byte)
	Next
	
	If header<>RM2_HEADER Then
		RuntimeError("Error while loading "+fullFilename+": expected "+RM2_HEADER+", found "+header)
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
	
	Local range#
	Local intensity#
	
	Local pitch#
	Local yaw#
	Local roll#
	
	Local innerConeAngle#
	Local outerConeAngle#
	
	Local ambienceInd%
	
	Local propName$
	Local xScale#
	Local yScale#
	Local zScale#
	
	Local prevType%
	While Not Eof(file)
		prevType = partType
		partType = ReadByte(file)
		WriteByte(writer,partType)
		Select partType
			Case RM2_TEXTURES
				;[Block]
				count = ReadByte(file) : WriteByte(writer,count)
				For i = 0 To count-1
					texName = ReadByteString(file) : WriteByteString(writer,TrimExtension(texName))
					flags = ReadByte(file) : WriteByte(writer,flags)
					uvSet = ReadByte(file) : WriteByte(writer,uvSet)
				Next
				;[End Block]
			Case RM2_OPAQUE,RM2_ALPHA
				;[Block]
				For i = 0 To 1
					WriteByte(writer,ReadByte(file))
				Next
				
				;vertices
				count = ReadShort(file) : WriteShort(writer,count)
				For i=0 To count-1
					x = ReadFloat(file) : y = ReadFloat(file) : z = ReadFloat(file)
					WriteFloat(writer,x) : WriteFloat(writer,y) : WriteFloat(writer,z)
					
					r = ReadByte(file) : g = ReadByte(file) : b = ReadByte(file)
					WriteByte(writer,r) : WriteByte(writer,g) : WriteByte(writer,b)
					
					For j=0 To 1
						u = ReadFloat(file) : v = ReadFloat(file)
						WriteFloat(writer,u) : WriteFloat(writer,v)
					Next
				Next
				
				;triangles
				count = ReadShort(file) : WriteShort(writer,count)
				For i=0 To count-1
					vert1 = ReadShort(file) : WriteShort(writer,vert1)
					vert2 = ReadShort(file) : WriteShort(writer,vert2)
					vert3 = ReadShort(file) : WriteShort(writer,vert3)
				Next
				;[End Block]
			Case RM2_INVISIBLE
				;[Block]
				;vertices
				count = ReadShort(file) : WriteShort(writer,count)
				
				For i=0 To count-1
					x = ReadFloat(file) : y = ReadFloat(file) : z = ReadFloat(file)
					WriteFloat(writer,x) : WriteFloat(writer,y) : WriteFloat(writer,z)
				Next
				
				;triangles
				count = ReadShort(file) : WriteShort(writer,count)
				If Instr(fullFilename,"_sl_")>0 Then
					DebugLog("HYPE "+count)
				EndIf
				
				For i=0 To count-1
					vert1 = ReadShort(file) : WriteShort(writer,vert1)
					vert2 = ReadShort(file) : WriteShort(writer,vert2)
					vert3 = ReadShort(file) : WriteShort(writer,vert3)
				Next
				;[End Block]
			Case RM2_SCREEN
				;[Block]
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				WriteByteString(writer,TrimExtension(ReadByteString(file)))
				;[End Block]
			Case RM2_WAYPOINT
				;[Block]
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				For i = 0 To 16
					byte = ReadByte(file) : WriteByte(writer,byte)
					If byte=0 Then Exit
				Next
				;[End Block]
			Case RM2_POINTLIGHT
				;[Block]
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				
				WriteFloat(writer,ReadFloat(file))
				
				WriteByte(writer,ReadByte(file))
				WriteByte(writer,ReadByte(file))
				WriteByte(writer,ReadByte(file))
				
				WriteByte(writer,ReadByte(file))
				;[End Block]
			Case RM2_SPOTLIGHT
				;[Block]
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				
				WriteFloat(writer,ReadFloat(file))
				
				WriteByte(writer,ReadByte(file))
				WriteByte(writer,ReadByte(file))
				WriteByte(writer,ReadByte(file))
				
				WriteByte(writer,ReadByte(file))
				
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				;[End Block]
			Case RM2_SOUNDEMITTER
				;[Block]
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				
				WriteByte(writer,ReadByte(file))
				
				WriteFloat(writer,ReadFloat(file))
				;[End Block]
			Case RM2_PROP
				;[Block]
				WriteByteString(writer,TrimExtension(ReadByteString(file)))
				
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				WriteFloat(writer,ReadFloat(file))
				;[End Block]
			Default
				RuntimeError("Error after reading type "+prevType)
		End Select
	Wend
	
	CloseFile(file)
	CloseFile(writer)
End Function

Function LoadRoomTemplates(file$)
	r% = ReadFile(file)
	While Not Eof(r)
		ln$ = ReadLine(r)
		If (Left(ln,1)="[") And (Instr(ln,"room amb")=0) Then
			ln = Mid(ln, 2, Len(ln) - 2)
			meshpath$ = GetINIString(file,ln,"meshpath")
			DebugLog("FIXING: "+meshpath)
			FixRM2Props(meshpath)
			DeleteFile(meshpath)
			CopyFile("temp.rm2",meshpath)
			DeleteFile("temp.rm2")
		EndIf
	Wend
End Function

ChangeDir("..")
LoadRoomTemplates("Data/rooms.ini")

;~IDEal Editor Parameters:
;~C#Blitz3D