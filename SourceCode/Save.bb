

Function SaveGame(file$)
	RuntimeError "TODO: reimplement"
End Function

Function LoadGame(file$,quick%=False)
	RuntimeError "TODO: reimplement"
End Function

Function LoadSaveGames()
	Local file$
	
	SaveGameAmount = 0
	If FileType(SavePath)=1 Then RuntimeError "Can't create dir "+Chr(34)+SavePath+Chr(34)
	If FileType(SavePath)=0 Then CreateDir(SavePath)
	Local myDir% = ReadDir(SavePath) 
	Repeat 
		file$=NextFile$(myDir) 
		If file$="" Then Exit 
		If FileType(SavePath+"/"+file$) = 2 Then 
			If file <> "." And file <> ".." Then 
				If (FileType(SavePath + file + "/save.txt")>0) Then
					SaveGameAmount=SaveGameAmount+1
				EndIf
			EndIf
		EndIf 
	Forever 
	CloseDir myDir 
	
	Dim SaveGames$(SaveGameAmount+1) 
	
	myDir=ReadDir(SavePath) 
	Local i% = 0
	Repeat 
		file$=NextFile$(myDir) 
		If file$="" Then Exit 
		If FileType(SavePath+"/"+file$) = 2 Then 
			If file <> "." And file <> ".." Then 
				If (FileType(SavePath + file + "/save.txt")>0) Then
					SaveGames(i) = file
					i=i+1
				EndIf
			EndIf
		EndIf 
	Forever 
	CloseDir myDir 
	
	Dim SaveGameTime$(SaveGameAmount + 1)
	Dim SaveGameDate$(SaveGameAmount + 1)
	For i = 1 To SaveGameAmount
		DebugLog (SavePath + SaveGames(i - 1) + "/save.txt")
		Local f% = ReadFile(SavePath + SaveGames(i - 1) + "/save.txt")
		SaveGameTime(i - 1) = ReadString(f)
		SaveGameDate(i - 1) = ReadString(f)
		CloseFile f
	Next
End Function


Function LoadSavedMaps()
	Local i, Dir, file$
	
	For i = 0 To MAXSAVEDMAPS-1
		SavedMaps(i)=""
	Next
	
	Dir=ReadDir("Map Creator/Maps") 
	i = 0
	Repeat 
		file$=NextFile$(Dir)
		
		DebugLog file
		
		If file$="" Then Exit 
		DebugLog (CurrentDir()+"Map Creator/Maps/"+file$)
		If FileType(CurrentDir()+"Map Creator/Maps/"+file$) = 1 Then 
			If file <> "." And file <> ".." Then 
				SavedMaps(i) = Left(file,Max(Len(file)-6,1))
				DebugLog i+": "+file
				i=i+1
			EndIf
			
			If i > MAXSAVEDMAPS Then Exit
		EndIf 
	Forever 
	CloseDir Dir 
End Function

Function LoadMap(file$)
	RuntimeError "REIMPLEMENT"
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D