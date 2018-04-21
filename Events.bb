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
	CreateEvent("alarm", "cont_173_1", 0)
	
	CreateEvent("pocketdimension", "pocketdimension", 0)	
	
	;there's a 7% chance that 106 appears in the rooms named "tnnl_plain_2"
	CreateEvent("tunnel106", "tnnl_plain_2", 0, 0.07 + (0.1*SelectedDifficulty\aggressiveNPCs))
	
	;the chance for 173 appearing in the first lockroom is about 66%
	;there's a 30% chance that it appears in the later lockrooms
	If Rand(3)<3 Then CreateEvent("lockroom173", "lck_cam_2c", 0)
	CreateEvent("lockroom173", "lck_cam_2c", 0, 0.3 + (0.5*SelectedDifficulty\aggressiveNPCs))
	
	CreateEvent("room2trick", "hll_plain_2", 0, 0.15)	
	
	CreateEvent("scp_970_2", "scp_970_2", 0)	
	
	;096 spawns in the first (and last) lockroom2
	CreateEvent("lockroom096", "lck_096_2c", 0)
	
	CreateEvent("endroom106", "end_1", Rand(0,1))
	
	CreateEvent("off_l_conf_2", "off_l_conf_2", 0)
	
	CreateEvent("room2fan", "hll_fan_2", 0, 1.0)
	
	CreateEvent("room2elevator2", "hll_ele_2", 0)
	;CreateEvent("hll_ele_2", "hll_ele_2", 0, 1)
	CreateEvent("hll_ele_2", "hll_ele_2", Rand(1,2))
	
	CreateEvent("strg_939_2", "strg_939_2", 0, 0)
	
	CreateEvent("tunnel2smoke", "tnnl_elec_2", 0, 0.2)
	CreateEvent("tnnl_elec_2", "tnnl_elec_2", Rand(0,2), 0)
	CreateEvent("tnnl_elec_2", "tnnl_elec_2", 0, (0.2*SelectedDifficulty\aggressiveNPCs))
	
	;173 appears in half of the "lck_tshape_2" -rooms
	CreateEvent("room2doors173", "lck_tshape_2", 0, 0.5 + (0.4*SelectedDifficulty\aggressiveNPCs))
	
	;the anomalous duck in room2offices2-rooms
	CreateEvent("off_lower_2", "off_lower_2", 0, 0.7)
	
	CreateEvent("closets_2", "closets_2", 0)	
	
	CreateEvent("hll_caf_2", "hll_caf_2", 0)	
	
	CreateEvent("room3pitduck", "hll_gas_3", 0)
	CreateEvent("room3pit1048", "hll_gas_3", 1)
	
	;the event that causes the door to open by itself in room2offices3
	CreateEvent("off_2level_2", "off_2level_2", 0, 1.0)	
	
	CreateEvent("srvr_096_2", "srvr_096_2", 0)	
	
	CreateEvent("srvr_farm_3", "srvr_farm_3", 0)	
	CreateEvent("srvr_farm_3", "srvr_lshape_3", 0)
	
	;the dead guard
	CreateEvent("tnnl_plain_3","tnnl_plain_3", 0, 0.08)
	
	CreateEvent("hll_plain_4","hll_plain_4", 0)
	
	If Rand(5)<5 Then 
		Select Rand(3)
			Case 1
				CreateEvent("682roar", "tnnl_plain_2", Rand(0,2), 0)	
			Case 2
				CreateEvent("682roar", "hll_gas_3", Rand(0,2), 0)		
			Case 3
				CreateEvent("682roar", "off_plain_2", 0, 0)	
		End Select 
	EndIf 
	
	CreateEvent("testroom173", "room2testroom2", 0, 1.0)	
	
	CreateEvent("tsl_ez_2", "tsl_ez_2", 0, 0.9)
	
	CreateEvent("tunnl_nuke_2", "tunnl_nuke_2", 0, 0)
	
	If Rand(5) < 5 Then 
		CreateEvent("coffin106", "coffin", 0, 0)
	Else
		CreateEvent("coffin", "coffin", 0, 0)
	EndIf 
	
	CreateEvent("checkpoint", "chck_lcz_hcz_2", 0, 1.0)
	CreateEvent("checkpoint", "chck_hcz_ez_2", 0, 1.0)
	
	CreateEvent("room3door", "hll_plain_3", 0, 0.1)
	CreateEvent("room3door", "tnnl_plain_3", 0, 0.1)	
	
	If Rand(2)=1 Then
		CreateEvent("106victim", "hll_plain_3", Rand(1,2))
		CreateEvent("106sinkhole", "hll_pipes_3", Rand(2,3))
	Else
		CreateEvent("106victim", "hll_pipes_3", Rand(1,2))
		CreateEvent("106sinkhole", "hll_plain_3", Rand(2,3))
	EndIf
	CreateEvent("106sinkhole", "hll_plain_4", Rand(1,2))
	
	CreateEvent("cont_079_1", "cont_079_1", 0, 0)	
	
	CreateEvent("cont_049_2", "cont_049_2", 0, 0)
	
	CreateEvent("cont_012_2", "cont_012_2", 0, 0)
	
	CreateEvent("cont_035_1", "cont_035_1", 0, 0)
	
	CreateEvent("cont_008_1", "cont_008_1", 0, 0)
	
	CreateEvent("cont_106_1", "cont_106_1", 0, 0)	
	
	CreateEvent("pj", "roompj", 0, 0)
	
	CreateEvent("cont_914_1", "cont_914_1", 0, 0)
	
	CreateEvent("toiletguard", "hll_toilets_2", 1)
	CreateEvent("buttghost", "hll_toilets_2", 0, 0.8)
	
	CreateEvent("room2pipes106", "tnnl_pipes_2", Rand(0, 3)) 
	
	CreateEvent("hll_gas_2", "hll_gas_2", 0, 0.4 + (0.4*SelectedDifficulty\aggressiveNPCs))
	
	CreateEvent("test_682_2", "test_682_2", 0)
	
	CreateEvent("tnnl_maintenance_2", "tnnl_maintenance_2", 0)
	
	CreateEvent("strg_elec_2c", "strg_elec_2c", 0)
	
	CreateEvent("exit_gatea_1", "exit_gatea_1", 0)
	CreateEvent("extend_gatea_1", "extend_gatea_1", 0)	
	CreateEvent("exit1", "exit1", 0)
	
	CreateEvent("cont_205_1", "cont_205_1", 0)
	
	CreateEvent("test_860_2","test_860_2", 0)
	
	CreateEvent("cont_966_3","cont_966_3", 0)
	
	CreateEvent("cont_1123_2", "cont_1123_2", 0, 0)
	;CreateEvent("room2test1074","room2test1074",0)
	;CreateEvent("room038","room038",0,0)
	;CreateEvent("room009","room009",0,0)
	;CreateEvent("medibay", "medibay", 0)
	;CreateEvent("room409", "room409", 0)
	;CreateEvent("room020", "room020", 0)
	CreateEvent("tsl_ez_2", "tsl_lcz_2", 0, 0.9)
	CreateEvent("tsl_ez_2", "room2tesla_hcz", 0, 0.9)
	
	CreateEvent("tnnl_plain_4","tnnl_plain_4",0)
	CreateEvent("room_gw","lck_air_2",0,1.0)
	CreateEvent("dimension1499","dimension1499",0)
	CreateEvent("cont_1162_2c","cont_1162_2c",0)
	CreateEvent("cont_500_1499_2","cont_500_1499_2",0)
	CreateEvent("room_gw","lck_ez_3",0,1.0)
	CreateEvent("hll_sl_2","hll_sl_2",0)
	
	CreateEvent("lck_air_broke_2","lck_air_broke_2",Rand(0,1))
	
	CreateEvent("096spawn","hll_gas_4",0,0.6+(0.2*SelectedDifficulty\aggressiveNPCs))
	CreateEvent("096spawn","hll_gas_3",0,0.6+(0.2*SelectedDifficulty\aggressiveNPCs))
	CreateEvent("096spawn","tnnl_pipes_2",0,0.4+(0.2*SelectedDifficulty\aggressiveNPCs))
	CreateEvent("096spawn","hll_gas_2",0,0.5+(0.2*SelectedDifficulty\aggressiveNPCs))
	CreateEvent("096spawn","tnnl_plain_3",0,0.6+(0.2*SelectedDifficulty\aggressiveNPCs))
	CreateEvent("096spawn","tnnl_plain_4",0,0.7+(0.2*SelectedDifficulty\aggressiveNPCs))
	CreateEvent("096spawn","tnnl_plain_2",0,0.6+(0.2*SelectedDifficulty\aggressiveNPCs))
	CreateEvent("096spawn","tnnl_elec_2",0,0.4+(0.2*SelectedDifficulty\aggressiveNPCs))
	CreateEvent("096spawn","hll_dirty_3",0,0.7+(0.2*SelectedDifficulty\aggressiveNPCs))
	
	CreateEvent("hll_gas_2","hll_lshape_2",0,0.4 + (0.4*SelectedDifficulty\aggressiveNPCs))
	
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
				UpdateEvent_pocketdimension(e)
			Case "tunnel106"
				UpdateEventTunnel106(e)
			Case "lockroom173"
				UpdateEventLockroom173(e)
			Case "room2trick"
				UpdateEventRoom2trick(e)
			Case "scp_970_2"
				UpdateEvent_scp_970_2(e)
			Case "lockroom096"
				UpdateEventLockroom096(e)
			Case "endroom106"
				UpdateEventEndroom106(e)
			Case "off_l_conf_2"
				UpdateEvent_off_l_conf_2(e)
			Case "room2fan"
				UpdateEventRoom2fan(e)
			Case "room2elevator2"
				UpdateEventRoom2elevator2(e)
			Case "hll_ele_2"
				UpdateEvent_hll_ele_2(e)
			Case "strg_939_2"
				UpdateEvent_strg_939_2(e)
			Case "tunnel2smoke"
				UpdateEventTunnel2smoke(e)
			Case "tnnl_elec_2"
				UpdateEvent_tnnl_elec_2(e)
			Case "room2doors173"
				UpdateEventRoom2doors173(e)
			Case "off_lower_2"
				UpdateEvent_off_lower_2(e)
			Case "closets_2"
				UpdateEvent_closets_2(e)
			Case "hll_caf_2"
				UpdateEvent_hll_caf_2(e)
			Case "room3pitduck"
				UpdateEventRoom3pitduck(e)
			Case "room3pit1048"
				UpdateEventRoom3pit1048(e)
			Case "off_2level_2"
				UpdateEvent_off_2level_2(e)
			Case "srvr_096_2"
				UpdateEvent_srvr_096_2(e)
			Case "srvr_farm_3"
				UpdateEvent_srvr_farm_3(e)
			Case "tnnl_plain_3"
				UpdateEvent_tnnl_plain_3(e)
			Case "hll_plain_4"
				UpdateEvent_hll_plain_4(e)
			Case "682roar"
				UpdateEvent682roar(e)
			Case "testroom173"
				UpdateEventTestroom173(e)
			Case "tsl_ez_2"
				UpdateEvent_tsl_ez_2(e)
			Case "tunnl_nuke_2"
				UpdateEvent_tunnl_nuke_2(e)
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
			Case "cont_079_1"
				UpdateEvent_cont_079_1(e)
			Case "cont_049_2"
				UpdateEvent_cont_049_2(e)
			Case "cont_012_2"
				UpdateEvent_cont_012_2(e)
			Case "cont_035_1"
				UpdateEvent_cont_035_1(e)
			Case "cont_008_1"
				UpdateEvent_cont_008_1(e)
			Case "cont_106_1"
				UpdateEvent_cont_106_1(e)
			Case "cont_914_1"
				UpdateEvent_cont_914_1(e)
			Case "toiletguard"
				UpdateEventToiletguard(e)
			Case "buttghost"
				UpdateEventButtghost(e)
			Case "room2pipes106"
				UpdateEventRoom2pipes106(e)
			Case "hll_gas_2"
				UpdateEvent_hll_gas_2(e)
			Case "test_682_2"
				UpdateEvent_test_682_2(e)
			Case "tnnl_maintenance_2"
				UpdateEvent_tnnl_maintenance_2(e)
			Case "strg_elec_2c"
				UpdateEvent_strg_elec_2c(e)
			Case "exit_gatea_1"
				UpdateEvent_exit_gatea_1(e)
			Case "extend_gatea_1"
				UpdateEvent_extend_gatea_1(e)
			Case "exit1"
				UpdateEventExit1(e)
			Case "cont_205_1"
				UpdateEvent_cont_205_1(e)
			Case "test_860_2"
				UpdateEvent_test_860_2(e)
			Case "cont_966_3"
				UpdateEvent_cont_966_3(e)
			Case "cont_1123_2"
				UpdateEvent_cont_1123_2(e)
			Case "tnnl_plain_4"
				UpdateEvent_tnnl_plain_4(e)
			Case "room_gw"
				UpdateEventRoom_gw(e)
			Case "dimension1499"
				UpdateEvent_dimension1499(e)
			Case "cont_1162_2c"
				UpdateEvent_cont_1162_2c(e)
			Case "cont_500_1499_2"
				UpdateEvent_cont_500_1499_2(e)
			Case "hll_sl_2"
				UpdateEvent_hll_sl_2(e)
			Case "lck_air_broke_2"
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