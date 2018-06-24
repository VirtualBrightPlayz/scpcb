fuck% = ReadFile("Data/rooms.ini")
ln$ = ""
prevln$ = ""
While Not Eof(fuck)
	prevln = ln
	ln = Lower(Trim(ReadLine(fuck)))
	If Len(ln)>0 And Len(prevln)>0 Then
		If Left(ln,1) = ";" And Left(prevln,1)="[" Then
			ln = Right(ln,Len(ln)-1)
			prevln = Left(prevln,Len(prevln)-1)
			prevln = Right(prevln,Len(prevln)-1)
			DebugLog(ln+" - "+prevln)
			
			dir% = ReadDir("Rooms")
			fn$ = NextFile(dir)
			While fn<>""
				If FileType("Rooms/"+fn)=1 Then
					cond% = Instr(Lower(fn),"room"+ln+".")>0 Or Left(Lower(fn),Len(ln)+1)=ln+"."
					If cond Then
						CopyFile("Rooms/"+fn,"NewRooms/Room_"+prevln+"TEMP.bb")
						
						rread% = ReadFile("NewRooms/Room_"+prevln+"TEMP.bb")
						rwrite% = WriteFile("NewRooms/Room_"+prevln+".bb")
						While Not Eof(rread)
							asd$ = CaseInsensitiveReplace(ReadLine(rread),"FillRoom"+ln+"(","FillRoom_"+prevln+"(")
							asd = CaseInsensitiveReplace(asd,"Fill"+ln+"(","FillRoom_"+prevln+"(")
							asd = CaseInsensitiveReplace(asd,"UpdateEventRoom"+ln+"(","UpdateEvent_"+prevln+"(")
							asd = CaseInsensitiveReplace(asd,"UpdateEvent"+ln+"(","UpdateEvent_"+prevln+"(")
							WriteLine(rwrite,asd)
						Wend
						CloseFile(rread)
						CloseFile(rwrite)
						DeleteFile("NewRooms/Room_"+prevln+"TEMP.bb")
						
						;DebugLog(fn+"!!!!!!")
						DeleteFile("Rooms/"+fn)
						Exit
					EndIf
				EndIf
				fn = NextFile(dir)
			Wend
			CloseDir(dir)
			
			mapsystem% = ReadFile("MapSystem.bb")
			mapsystemalt% = WriteFile("MapSystemAlt.bb")
			While Not Eof(mapsystem)
				msln$ = ReadLine(mapsystem)
				msln = CaseInsensitiveReplace(msln,"FillRoom"+ln+"(","FillRoom_"+prevln+"(")
				msln = CaseInsensitiveReplace(msln,"Fill"+ln+"(","FillRoom_"+prevln+"(")
				WriteLine(mapsystemalt,CaseInsensitiveReplace(msln,Chr(34)+ln+Chr(34),Chr(34)+prevln+Chr(34)))
			Wend
			CloseFile(mapsystem)
			CloseFile(mapsystemalt)
			DeleteFile("MapSystem.bb")
			CopyFile("MapSystemAlt.bb","MapSystem.bb")
			DeleteFile("MapSystemAlt.bb")
			
			events% = ReadFile("Events.bb")
			eventsalt% = WriteFile("EventsAlt.bb")
			While Not Eof(events)
				msln$ = ReadLine(events)
				msln = CaseInsensitiveReplace(msln,"FillRoom"+ln+"(","FillRoom_"+prevln+"(")
				msln = CaseInsensitiveReplace(msln,"Fill"+ln+"(","FillRoom_"+prevln+"(")
				msln = CaseInsensitiveReplace(msln,"UpdateEventRoom"+ln+"(","UpdateEvent_"+prevln+"(")
				msln = CaseInsensitiveReplace(msln,"UpdateEvent"+ln+"(","UpdateEvent_"+prevln+"(")
				WriteLine(eventsalt,CaseInsensitiveReplace(msln,Chr(34)+ln+Chr(34),Chr(34)+prevln+Chr(34)))
			Wend
			CloseFile(events)
			CloseFile(eventsalt)
			DeleteFile("Events.bb")
			CopyFile("EventsAlt.bb","Events.bb")
			DeleteFile("EventsAlt.bb")
		EndIf
	EndIf
Wend

includes% = WriteFile("RoomIncludes.txt")
dir% = ReadDir("NewRooms")
fn$ = NextFile(dir)
While fn<>""
	If FileType("NewRooms/"+fn)=1 Then
		WriteLine(includes,"Include "+Chr(34)+"Rooms/"+fn+Chr(34))
	EndIf
	fn$ = NextFile(dir)
Wend
CloseFile(includes)

Function CaseInsensitiveReplace$(dest$,find$,rplc$)
	If Lower(find)=Lower(rplc) Then Return dest
	pos% = Instr(Lower(dest),Lower(find))
	If pos>0 Then
		piece1$ = Left(dest,pos-1)
		piece2$ = Right(dest,Len(dest)-(pos-1)-Len(find))
		Return CaseInsensitiveReplace(piece1+rplc+piece2,find,rplc)
	EndIf
	Return dest
End Function

Function CapFirst$(s$)
	Return Upper(Left(s,1))+Right(s,Len(s)-1)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D