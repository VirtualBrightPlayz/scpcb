Function CreateEvent.Events(eventname$, roomname$, id%, prob# = 0.0)
	;roomname = the name of the room(s) you want the event to be assigned to
	
	;the id-variable determines which of the rooms the event is assigned to,
	;0 will assign it to the first generated room, 1 to the second, etc
	
	;the prob-variable can be used to randomly assign events into some rooms
	;0.5 means that there's a 50% chance that event is assigned to the rooms
	;1.0 means that the event is assigned to every room
	;the id-variable is ignored if prob <> 0.0
	
	Local i% = 0, temp%, e.Events, e2.Events, r.Rooms
	
	If prob = 0.0 Then
		For r.Rooms = Each Rooms
			If (roomname = "" Or roomname = r\RoomTemplate\Name) Then
				temp = False
				For e2.Events = Each Events
					If e2\room = r Then temp = True : Exit
				Next
				
				i=i+1
				If i >= id And temp = False Then
					e.Events = New Events
					e\EventName = eventname					
					e\room = r
					Return e
				End If
			EndIf
		Next
	Else
		For r.Rooms = Each Rooms
			If (roomname = "" Or roomname = r\RoomTemplate\Name) Then
				temp = False
				For e2.Events = Each Events
					If e2\room = r Then temp = True : Exit
				Next
				
				If Rnd(0.0, 1.0) < prob And temp = False Then
					e.Events = New Events
					e\EventName = eventname					
					e\room = r
				End If
			EndIf
		Next		
	EndIf
	
	Return Null
End Function

Function RemoveEvent(e.Events)
	If e\Sound<>0 Then FreeSound_Strict e\Sound
	If e\Sound2<>0 Then FreeSound_Strict e\Sound2
	If e\img<>0 Then FreeImage e\img
	Delete e
End Function

Function InitEvents()
	Local e.Events
	
	CreateEvent("173", "roomintro", 0)
	CreateEvent("alarm", "room173", 0)
	
	CreateEvent("pocketdimension", "pocketdimension", 0)	
	
	;there's a 7% chance that 106 appears in the rooms named "tunnel"
	CreateEvent("tunnel106", "tunnel", 0, 0.07 + (0.1*SelectedDifficulty\aggressiveNPCs))
	
	;the chance for 173 appearing in the first lockroom is about 66%
	;there's a 30% chance that it appears in the later lockrooms
	If Rand(3)<3 Then CreateEvent("lockroom173", "lockroom", 0)
	CreateEvent("lockroom173", "lockroom", 0, 0.3 + (0.5*SelectedDifficulty\aggressiveNPCs))
	
	CreateEvent("room2trick", "room2", 0, 0.15)	
	
	CreateEvent("room2storage", "room2storage", 0)	
	
	;096 spawns in the first (and last) lockroom2
	CreateEvent("lockroom096", "lockroom2", 0)
	
	CreateEvent("endroom106", "endroom", Rand(0,1))
	
	CreateEvent("room2poffices2", "room2poffices2", 0)
	
	CreateEvent("room2fan", "room2_2", 0, 1.0)
	
	CreateEvent("room2elevator2", "room2elevator", 0)
	;CreateEvent("room2elevator", "room2elevator", 0, 1)
	CreateEvent("room2elevator", "room2elevator", Rand(1,2))
	
	CreateEvent("room3storage", "room3storage", 0, 0)
	
	CreateEvent("tunnel2smoke", "tunnel2", 0, 0.2)
	CreateEvent("tunnel2", "tunnel2", Rand(0,2), 0)
	CreateEvent("tunnel2", "tunnel2", 0, (0.2*SelectedDifficulty\aggressiveNPCs))
	
	;173 appears in half of the "room2doors" -rooms
	CreateEvent("room2doors173", "room2doors", 0, 0.5 + (0.4*SelectedDifficulty\aggressiveNPCs))
	
	;the anomalous duck in room2offices2-rooms
	CreateEvent("room2offices2", "room2offices2", 0, 0.7)
	
	CreateEvent("room2closets", "room2closets", 0)	
	
	CreateEvent("room2cafeteria", "room2cafeteria", 0)	
	
	CreateEvent("room3pitduck", "room3pit", 0)
	CreateEvent("room3pit1048", "room3pit", 1)
	
	;the event that causes the door to open by itself in room2offices3
	CreateEvent("room2offices3", "room2offices3", 0, 1.0)	
	
	CreateEvent("room2servers", "room2servers", 0)	
	
	CreateEvent("room3servers", "room3servers", 0)	
	CreateEvent("room3servers", "room3servers2", 0)
	
	;the dead guard
	CreateEvent("room3tunnel","room3tunnel", 0, 0.08)
	
	CreateEvent("room4","room4", 0)
	
	If Rand(5)<5 Then 
		Select Rand(3)
			Case 1
				CreateEvent("682roar", "tunnel", Rand(0,2), 0)	
			Case 2
				CreateEvent("682roar", "room3pit", Rand(0,2), 0)		
			Case 3
				CreateEvent("682roar", "room2offices", 0, 0)	
		End Select 
	EndIf 
	
	CreateEvent("testroom173", "room2testroom2", 0, 1.0)	
	
	CreateEvent("room2tesla", "room2tesla", 0, 0.9)
	
	CreateEvent("room2nuke", "room2nuke", 0, 0)
	
	If Rand(5) < 5 Then 
		CreateEvent("coffin106", "coffin", 0, 0)
	Else
		CreateEvent("coffin", "coffin", 0, 0)
	EndIf 
	
	CreateEvent("checkpoint", "checkpoint1", 0, 1.0)
	CreateEvent("checkpoint", "checkpoint2", 0, 1.0)
	
	CreateEvent("room3door", "room3", 0, 0.1)
	CreateEvent("room3door", "room3tunnel", 0, 0.1)	
	
	If Rand(2)=1 Then
		CreateEvent("106victim", "room3", Rand(1,2))
		CreateEvent("106sinkhole", "room3_2", Rand(2,3))
	Else
		CreateEvent("106victim", "room3_2", Rand(1,2))
		CreateEvent("106sinkhole", "room3", Rand(2,3))
	EndIf
	CreateEvent("106sinkhole", "room4", Rand(1,2))
	
	CreateEvent("room079", "room079", 0, 0)	
	
	CreateEvent("room049", "room049", 0, 0)
	
	CreateEvent("room012", "room012", 0, 0)
	
	CreateEvent("room035", "room035", 0, 0)
	
	CreateEvent("008", "008", 0, 0)
	
	CreateEvent("room106", "room106", 0, 0)	
	
	CreateEvent("pj", "roompj", 0, 0)
	
	CreateEvent("914", "914", 0, 0)
	
	CreateEvent("toiletguard", "room2toilets", 1)
	CreateEvent("buttghost", "room2toilets", 0, 0.8)
	
	CreateEvent("room2pipes106", "room2pipes", Rand(0, 3)) 
	
	CreateEvent("room2pit", "room2pit", 0, 0.4 + (0.4*SelectedDifficulty\aggressiveNPCs))
	
	CreateEvent("testroom", "testroom", 0)
	
	CreateEvent("room2tunnel", "room2tunnel", 0)
	
	CreateEvent("room2ccont", "room2ccont", 0)
	
	CreateEvent("gateaentrance", "gateaentrance", 0)
	CreateEvent("gatea", "gatea", 0)	
	CreateEvent("exit1", "exit1", 0)
	
	CreateEvent("room205", "room205", 0)
	
	CreateEvent("room860","room860", 0)
	
	CreateEvent("room966","room966", 0)
	
	CreateEvent("room1123", "room1123", 0, 0)
	;CreateEvent("room2test1074","room2test1074",0)
	;CreateEvent("room038","room038",0,0)
	;CreateEvent("room009","room009",0,0)
	;CreateEvent("medibay", "medibay", 0)
	;CreateEvent("room409", "room409", 0)
	;CreateEvent("room020", "room020", 0)
	CreateEvent("room2tesla", "room2tesla_lcz", 0, 0.9)
	CreateEvent("room2tesla", "room2tesla_hcz", 0, 0.9)
	
	CreateEvent("room4tunnels","room4tunnels",0)
	CreateEvent("room_gw","room2gw",0,1.0)
	CreateEvent("dimension1499","dimension1499",0)
	CreateEvent("room1162","room1162",0)
	CreateEvent("room2scps2","room2scps2",0)
	CreateEvent("room_gw","room3gw",0,1.0)
	CreateEvent("room2sl","room2sl",0)
	
	CreateEvent("room2gw_b","room2gw_b",Rand(0,1))
	
	CreateEvent("096spawn","room4pit",0,0.6+(0.2*SelectedDifficulty\aggressiveNPCs))
	CreateEvent("096spawn","room3pit",0,0.6+(0.2*SelectedDifficulty\aggressiveNPCs))
	CreateEvent("096spawn","room2pipes",0,0.4+(0.2*SelectedDifficulty\aggressiveNPCs))
	CreateEvent("096spawn","room2pit",0,0.5+(0.2*SelectedDifficulty\aggressiveNPCs))
	CreateEvent("096spawn","room3tunnel",0,0.6+(0.2*SelectedDifficulty\aggressiveNPCs))
	CreateEvent("096spawn","room4tunnels",0,0.7+(0.2*SelectedDifficulty\aggressiveNPCs))
	CreateEvent("096spawn","tunnel",0,0.6+(0.2*SelectedDifficulty\aggressiveNPCs))
	CreateEvent("096spawn","tunnel2",0,0.4+(0.2*SelectedDifficulty\aggressiveNPCs))
	CreateEvent("096spawn","room3z2",0,0.7+(0.2*SelectedDifficulty\aggressiveNPCs))
	
	CreateEvent("room2pit","room2_4",0,0.4 + (0.4*SelectedDifficulty\aggressiveNPCs))
	
End Function

Function UpdateEvents()
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%
	
	Local p.Particles, n.NPCs, r.Rooms, e.Events, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams
	
	Local CurrTrigger$ = ""
	
	Local x#, y#, z#
	
	Local angle#
	
    mainPlayer\footstepOverride = 0
	
	UpdateRooms()
	
    For e.Events = Each Events
		Select e\EventName
			Case "173"
				UpdateEvent173(e)
			Case "alarm"
				UpdateEventAlarm(e)
			Case "pocketdimension"
				UpdateEventPocketdimension(e)
			Case "tunnel106"
				UpdateEventTunnel106(e)
			Case "lockroom173"
				UpdateEventLockroom173(e)
			Case "room2trick"
				UpdateEventRoom2trick(e)
			Case "room2storage"
				UpdateEventRoom2storage(e)
			Case "lockroom096"
				UpdateEventLockroom096(e)
			Case "endroom106"
				UpdateEventEndroom106(e)
			Case "room2poffices2"
				UpdateEventRoom2poffices2(e)
			Case "room2fan"
				UpdateEventRoom2fan(e)
			Case "room2elevator2"
				UpdateEventRoom2elevator2(e)
			Case "room2elevator"
				UpdateEventRoom2elevator(e)
			Case "room3storage"
				UpdateEventRoom3storage(e)
			Case "tunnel2smoke"
				UpdateEventTunnel2smoke(e)
			Case "tunnel2"
				UpdateEventTunnel2(e)
			Case "room2doors173"
				UpdateEventRoom2doors173(e)
			Case "room2offices2"
				UpdateEventRoom2offices2(e)
			Case "room2closets"
				UpdateEventRoom2closets(e)
			Case "room2cafeteria"
				UpdateEventRoom2cafeteria(e)
			Case "room3pitduck"
				UpdateEventRoom3pitduck(e)
			Case "room3pit1048"
				UpdateEventRoom3pit1048(e)
			Case "room2offices3"
				UpdateEventRoom2offices3(e)
			Case "room2servers"
				UpdateEventRoom2servers(e)
			Case "room3servers"
				UpdateEventRoom3servers(e)
			Case "room3tunnel"
				UpdateEventRoom3tunnel(e)
			Case "room4"
				UpdateEventRoom4(e)
			Case "682roar"
				UpdateEvent682roar(e)
			Case "testroom173"
				UpdateEventTestroom173(e)
			Case "room2tesla"
				UpdateEventRoom2tesla(e)
			Case "room2nuke"
				UpdateEventRoom2nuke(e)
			Case "coffin106"
				UpdateEventCoffin106(e)
			Case "coffin"
				UpdateEventCoffin(e)
			Case "checkpoint"
				UpdateEventCheckpoint(e)
			Case "room3door"
				UpdateEventRoom3door(e)
			Case "106victim"
				UpdateEvent106victim(e)
			Case "106sinkhole"
				UpdateEvent106sinkhole(e)
			Case "room079"
				UpdateEventRoom079(e)
			Case "room049"
				UpdateEventRoom049(e)
			Case "room012"
				UpdateEventRoom012(e)
			Case "room035"
				UpdateEventRoom035(e)
			Case "008"
				UpdateEvent008(e)
			Case "room106"
				UpdateEventRoom106(e)
			Case "914"
				UpdateEvent914(e)
			Case "toiletguard"
				UpdateEventToiletguard(e)
			Case "buttghost"
				UpdateEventButtghost(e)
			Case "room2pipes106"
				UpdateEventRoom2pipes106(e)
			Case "room2pit"
				UpdateEventRoom2pit(e)
			Case "testroom"
				UpdateEventTestroom(e)
			Case "room2tunnel"
				UpdateEventRoom2tunnel(e)
			Case "room2ccont"
				UpdateEventRoom2ccont(e)
			Case "gateaentrance"
				UpdateEventGateaentrance(e)
			Case "gatea"
				UpdateEventGatea(e)
			Case "exit1"
				UpdateEventExit1(e)
			Case "room205"
				UpdateEventRoom205(e)
			Case "room860"
				UpdateEventRoom860(e)
			Case "room966"
				UpdateEventRoom966(e)
			Case "room1123"
				UpdateEventRoom1123(e)
			Case "room4tunnels"
				UpdateEventRoom4tunnels(e)
			Case "room_gw"
				UpdateEventRoom_gw(e)
			Case "dimension1499"
				UpdateEventDimension1499(e)
			Case "room1162"
				UpdateEventRoom1162(e)
			Case "room2scps2"
				UpdateEventRoom2scps2(e)
			Case "room2sl"
				UpdateEventRoom2sl(e)
			Case "room2gw_b"
				UpdateEventRoom2gw_b(e)
			Case "096spawn"
				UpdateEvent096spawn(e)
		End Select
	Next
	
	If ExplosionTimer > 0 Then
		ExplosionTimer = ExplosionTimer+timing\tickDuration
		
		If ExplosionTimer < 140.0 Then
			If ExplosionTimer-timing\tickDuration < 5.0 Then
				ExplosionSFX = LoadSound_Strict("SFX\Ending\GateB\Nuke1.ogg")
				PlaySound_Strict ExplosionSFX
				mainPlayer\camShake = 10.0
				ExplosionTimer = 5.0
			EndIf
			
			mainPlayer\camShake = CurveValue(ExplosionTimer/60.0,mainPlayer\camShake, 50.0)
		Else
			mainPlayer\camShake = Min((ExplosionTimer/20.0),20.0)
			If ExplosionTimer-timing\tickDuration < 140.0 Then
				mainPlayer\blinkTimer = 1.0
				ExplosionSFX = LoadSound_Strict("SFX\Ending\GateB\Nuke2.ogg")
				PlaySound_Strict ExplosionSFX				
				For i = 0 To 40
					p.Particles = CreateParticle(EntityX(mainPlayer\collider)+Rnd(-0.5,0.5),EntityY(mainPlayer\collider)-Rnd(0.2,1.5),EntityZ(mainPlayer\collider)+Rnd(-0.5,0.5),0, Rnd(0.2,0.6), 0.0, 350)	
					RotateEntity p\pvt,-90,0,0,True
					p\speed = Rnd(0.05,0.07)
				Next
			EndIf
			mainPlayer\lightFlash = Min((ExplosionTimer-160.0)/40.0,2.0)
			If ExplosionTimer > 160 Then Kill(mainPlayer); : EndingTimer = Min(mainPlayer\fallTimer,-0.1)
			If ExplosionTimer > 500 Then ExplosionTimer = 0
		EndIf
		
	EndIf
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D