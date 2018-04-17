

Function SaveGame(file$)
	RuntimeError "TODO: reimplement"
End Function

Function LoadGame(file$,quick%=False)
	RuntimeError "TODO: reimplement"
End Function

Function LoadSaveGames()
	SaveGameAmount = 0
	If FileType(SavePath)=1 Then RuntimeError "Can't create dir "+Chr(34)+SavePath+Chr(34)
	If FileType(SavePath)=0 Then CreateDir(SavePath)
	myDir=ReadDir(SavePath) 
	Repeat 
		file$=NextFile$(myDir) 
		If file$="" Then Exit 
		If FileType(SavePath+"\"+file$) = 2 Then 
			If file <> "." And file <> ".." Then 
				If (FileType(SavePath + file + "\save.txt")>0) Then
					SaveGameAmount=SaveGameAmount+1
				EndIf
			EndIf
		End If 
	Forever 
	CloseDir myDir 
	
	Dim SaveGames$(SaveGameAmount+1) 
	
	myDir=ReadDir(SavePath) 
	i = 0
	Repeat 
		file$=NextFile$(myDir) 
		If file$="" Then Exit 
		If FileType(SavePath+"\"+file$) = 2 Then 
			If file <> "." And file <> ".." Then 
				If (FileType(SavePath + file + "\save.txt")>0) Then
					SaveGames(i) = file
					i=i+1
				EndIf
			EndIf
		End If 
	Forever 
	CloseDir myDir 
	
	Dim SaveGameTime$(SaveGameAmount + 1)
	Dim SaveGameDate$(SaveGameAmount + 1)
	For i = 1 To SaveGameAmount
		DebugLog (SavePath + SaveGames(i - 1) + "\save.txt")
		Local f% = ReadFile(SavePath + SaveGames(i - 1) + "\save.txt")
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
	
	Dir=ReadDir("Map Creator\Maps") 
	i = 0
	Repeat 
		file$=NextFile$(Dir)
		
		DebugLog file
		
		If file$="" Then Exit 
		DebugLog (CurrentDir()+"Map Creator\Maps\"+file$)
		If FileType(CurrentDir()+"Map Creator\Maps\"+file$) = 1 Then 
			If file <> "." And file <> ".." Then 
				SavedMaps(i) = Left(file,Max(Len(file)-6,1))
				DebugLog i+": "+file
				i=i+1
			EndIf
			
			If i > MAXSAVEDMAPS Then Exit
		End If 
	Forever 
	CloseDir Dir 
End Function

Function LoadMap(file$)
	Local f%, x%, y%, name$, angle%, prob#
	Local r.Rooms, rt.RoomTemplates, e.Events
	
	f% = ReadFile(file+".cbmap")
	DebugLog file+".cbmap"
	
	While Not Eof(f)
		x = ReadByte(f)
		y = ReadByte(f)
		name$ = Lower(ReadString(f))
		
		angle = ReadByte(f)*90.0
		
		DebugLog x+", "+y+": "+name
		DebugLog "angle: "+angle
		
		For rt.RoomTemplates=Each RoomTemplates
			If Lower(rt\Name) = name Then
                
                r.Rooms = CreateRoom(0, rt\Shape, x * 8.0, 0, y * 8.0, name)
				DebugLog "createroom"
				
                r\angle = angle
                If rt\Shape = ROOM2C Then r\angle = r\angle+90 Else r\angle = r\angle-180
                
                TurnEntity(r\obj, 0, r\angle, 0)
                
                MapTemp(x,y)=1
                
                Exit
			EndIf
		Next
		
		name = ReadString(f)
		prob# = ReadFloat(f)
		
		If r<>Null Then
			If Rnd(0.0,1.0)<prob Or prob=0 Then
                e.Events = New Events
                e\EventName = name
                e\room = r   
			EndIf
		EndIf
		
	Wend
	
	CloseFile f
	
	temp = 0
	Local spacing# = 8.0
	For y = MapHeight - 1 To 1 Step - 1
		
		If y < MapHeight/3+1 Then
			zone=3
		ElseIf y < MapHeight*(2.0/3.0)-1
			zone=2
		Else
			zone=1
		EndIf
		
		For x = 1 To MapWidth - 2
			If MapTemp(x,y) > 0 Then
                If zone = 2 Then temp = 2 Else temp=0
                
                For r.Rooms = Each Rooms
					If Int(r\x/8.0)=x And Int(r\z/8.0)=y Then
						If MapTemp(x + 1, y) > 0 Then
							d.Doors = CreateDoor(r\zone, Float(x) * spacing + spacing / 2.0, 0, Float(y) * spacing, 90, r, Max(Rand(-3, 1), 0), temp)
							r\AdjDoor[0] = d
						EndIf
						
						If MapTemp(x, y + 1) > 0 Then
							d.Doors = CreateDoor(r\zone, Float(x) * spacing, 0, Float(y) * spacing + spacing / 2.0, 0, r, Max(Rand(-3, 1), 0), temp)
							r\AdjDoor[3] = d
						EndIf
						
						Exit
					EndIf
                Next
                
			End If
			
		Next
	Next   
	
	r = CreateRoom(0, ROOM1, 8, 0, (MapHeight-1) * 8, "173")
	r = CreateRoom(0, ROOM1, (MapWidth-1) * 8, 0, (MapHeight-1) * 8, "pocketdimension")
	r = CreateRoom(0, ROOM1, 0, 0, 8, "gatea")
	
	CreateEvent("173", "173", 0)
	CreateEvent("pocketdimension", "pocketdimension", 0)   
	CreateEvent("gatea", "gatea", 0)
	
	For r.Rooms = Each Rooms
		r\Adjacent[0]=Null
		r\Adjacent[1]=Null
		r\Adjacent[2]=Null
		r\Adjacent[3]=Null
		For r2.Rooms = Each Rooms
			If r<>r2 Then
				If r2\z=r\z Then
					If (r2\x)=(r\x+8.0) Then
						r\Adjacent[0]=r2
						If r\AdjDoor[0] = Null Then r\AdjDoor[0] = r2\AdjDoor[2]
					ElseIf (r2\x)=(r\x-8.0)
						r\Adjacent[2]=r2
						If r\AdjDoor[2] = Null Then r\AdjDoor[2] = r2\AdjDoor[0]
					EndIf
				ElseIf r2\x=r\x Then
					If (r2\z)=(r\z-8.0) Then
						r\Adjacent[1]=r2
						If r\AdjDoor[1] = Null Then r\AdjDoor[1] = r2\AdjDoor[3]
					ElseIf (r2\z)=(r\z+8.0)
						r\Adjacent[3]=r2
						If r\AdjDoor[3] = Null Then r\AdjDoor[3] = r2\AdjDoor[1]
					EndIf
				EndIf
			EndIf
			If (r\Adjacent[0]<>Null) And (r\Adjacent[1]<>Null) And (r\Adjacent[2]<>Null) And (r\Adjacent[3]<>Null) Then Exit
		Next
	Next
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D