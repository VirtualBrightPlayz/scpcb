dir% = ReadDir("Rooms")

file$ = NextFile(dir)
file$ = NextFile(dir)
file$ = NextFile(dir)

w% = WriteFile("AAAAAAAAA.bb")

While Left(file,1)<>"." And Len(file)>0
	DebugLog("Rooms/"+file)
	f% = ReadFile("Rooms/"+file)
	While Not Eof(f)
		ln$ = ReadLine(f)
		If Instr(ln,"Function")<>0 Then
			If Instr(ln,"End")<>0 Or Instr(ln,"UpdateEvent")<>0 Then
				Exit
			Else
				ln = Replace(ln,"Function FillRoom_","Case "+Chr(34))
				ln = Replace(ln,"(r.Room)",Chr(34))
				WriteLine(w,ln)
			EndIf
		ElseIf (Instr(ln,"CreateDoor")<>0) Then
			aln$ = ""
			comma% = 0
			For i = 1 To Len(ln)
				If Mid(ln,i,1)="," Then
					comma=comma+1
				EndIf
				If ((comma>=1 And Mid(ln,i,1)<>",") Or comma>1) And (comma<4) Then
					aln=aln+Mid(ln,i,1)
				EndIf
				If Mid(ln,i,1)="," And comma = 2 Then
					aln=aln+"50.0+"
				EndIf
			Next
			ln = "CreateWaypoint("+Trim(aln)+")"
			ln = Replace(ln," ","")
			ln = Replace(ln,"EntityX(r\obj)","r\x") : ln = Replace(ln,"EntityY(r\obj)","r\y") : ln = Replace(ln,"EntityZ(r\obj)","r\z")
			ln = Replace(ln,"r\x+","") : ln = Replace(ln,"r\y+","") : ln = Replace(ln,"r\z+","")
			ln = Replace(ln,"r\x,","0,") : ln = Replace(ln,"r\y,","0,") : ln = Replace(ln,"r\z,","0,")
			ln = Replace(ln,"r\x)","0)") : ln = Replace(ln,"r\y)","0)") : ln = Replace(ln,"r\z)","0)")
			ln = Replace(ln,"r\x","") : ln = Replace(ln,"r\y","") : ln = Replace(ln,"r\z","")
			ln = Replace(ln,"+-","-")
			ln = Replace(ln,"*RoomScale","")
			;WriteLine(w,"    "+ln)
		ElseIf (Instr(ln,"_hb")<>0) Then
			WriteLine(w,ln)
		EndIf
	Wend
	;WriteLine(w,"")
	CloseFile(f)
	file = NextFile(dir)
Wend

CloseFile(w)

CloseDir(dir)
;~IDEal Editor Parameters:
;~C#Blitz3D