Function Max(a%,b%)
	If a>b Then Return a
	Return b
End Function

Include "INI.bb"

Function LoadRoomTemplates(file$)
	r% = ReadFile(file)
	CopyFile(file,"Data/newRooms.ini")
	While Not Eof(r)
		ln$ = ReadLine(r)
		If (Left(ln,1)="[") And (Instr(ln,"room amb")=0) Then
			ln = Mid(ln, 2, Len(ln) - 2)
			meshpath$ = GetINIString(file,ln,"meshpath")
			CreateDir("newGFX/GFX/Map/"+ln)
			CopyRMesh meshpath,"newGFX/GFX/Map/"+ln+"/"+ln+".rmesh"
			DeleteFile(meshpath)
			PutINIValue("Data/newRooms.ini",ln,"meshpath","GFX/Map/"+ln+"/"+ln+".rmesh")
			meshpath = Replace(meshpath,".rmesh","")
			meshpath = Replace(meshpath,"_opt","") : meshpath = Replace(meshpath,"_opt","")
			counter% = 1
			While FileType(meshpath+"_lm"+counter+".png")=1
				CopyFile(meshpath+"_lm"+counter+".png","newGFX/GFX/Map/"+ln+"/"+ln+"_lm"+counter+".png")
				DeleteFile(meshpath+"_lm"+counter+".png")
				counter=counter+1
			Wend
			counter = 1
			While FileType(meshpath+"_lm"+counter+".bmp")=1
				CopyFile(meshpath+"_lm"+counter+".png","newGFX/GFX/Map/"+ln+"/"+ln+"_lm"+counter+".png")
				DeleteFile(meshpath+"_lm"+counter+".bmp")
				counter=counter+1
			Wend
		EndIf
	Wend
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

Function CopyRMesh(from$,tto$)
	r% = ReadFile(from)
	w% = WriteFile(tto)
	
	from = StripPath(from)
	from = Replace(from,".rmesh","")
	from = Replace(from,"_opt","") : from = Replace(from,"_opt","")
	tto = StripPath(tto)
	tto = Replace(tto,".rmesh","")
	
	stri$ = ReadString(r) : WriteString(w,stri)
	c% = ReadInt(r) : WriteInt(w,c)
	
	For i=1 To c
		For j=0 To 1
			temp1i=ReadByte(r) : WriteByte(w,temp1i)
			If temp1i<>0 Then
				temp1s$=ReadString(r)
				temp1s = Replace(temp1s,from,tto)
				WriteString(w,temp1s)
			EndIf
		Next
		
		count2%=ReadInt(r) ;vertices
		WriteInt(w,count2)
		
		For j=1 To count2
			;world coords
			x#=ReadFloat(r) : y#=ReadFloat(r) : z#=ReadFloat(r)
			WriteFloat(w,x) : WriteFloat(w,y) : WriteFloat(w,z)
			
			;texture coords
			For k=0 To 1
				u#=ReadFloat(r) : v#=ReadFloat(r)
				WriteFloat(w,u) : WriteFloat(w,v)
			Next
			
			;colors
			temp1i=ReadByte(r)
			temp2i=ReadByte(r)
			temp3i=ReadByte(r)
			WriteByte(w,temp1i) : WriteByte(w,temp2i) : WriteByte(w,temp3i)
		Next
		
		count2=ReadInt(r) ;polys
		WriteInt(w,count2)
		For j=1 To count2
			temp1i = ReadInt(r) : temp2i = ReadInt(r) : temp3i = ReadInt(r)
			WriteInt(w,temp1i) : WriteInt(w,temp2i) : WriteInt(w,temp3i)
		Next
	Next
	
	While Not Eof(r)
		WriteByte(w,ReadByte(r))
	Wend
	
	CloseFile(r)
	CloseFile(w)
End Function

LoadRoomTemplates("Data/rooms.ini")
;~IDEal Editor Parameters:
;~C#Blitz3D