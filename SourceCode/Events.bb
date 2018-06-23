Const EVENT_MAX_STATE_COUNT% = 5
Const EVENT_SOUND_COUNT% = 5
Const EVENT_CHANNEL_COUNT% = 2
Type Events
	Field name$
	Field room.Rooms

	Field eventState#, eventState2#, eventState3#

	Field loaded%

	;Field img% ;???

	Field soundChannels%[EVENT_CHANNEL_COUNT]
	Field sounds%[EVENT_SOUND_COUNT]

	Field musicTrack$
	Field overwriteMusic%
End Type

Function CreateEvent.Events(eventname$, roomname$, id%, prob# = 0.0)
	;roomname = the name of the room(s) you want the event to be assigned to

	;the id-variable determines which of the rooms the event is assigned to,
	;0 will assign it to the first generated room, 1 to the second, etc

	;the prob-variable can be used to randomly assign events into some rooms
	;0.5 means that there's a 50% chance that event is assigned to the rooms
	;1.0 means that the event is assigned to every room
	;the id-variable is ignored if prob <> 0.0

	Local i% = 0, temp%, e.Events, e2.Events, r.Rooms

	If (prob = 0.0) Then
		For r.Rooms = Each Rooms
			If ((roomname = "" Or roomname = r\roomTemplate\name)) Then
				temp = False
				For e2.Events = Each Events
					If (e2\room = r) Then
						temp = True : Exit
					EndIf
				Next

				i=i+1
				If (i >= id And temp = False) Then
					e.Events = New Events
					e\name = eventname
					e\room = r
					Return e
				EndIf
			EndIf
		Next
	Else
		For r.Rooms = Each Rooms
			If ((roomname = "" Or roomname = r\roomTemplate\name)) Then
				temp = False
				For e2.Events = Each Events
					If (e2\room = r) Then
						temp = True : Exit
					EndIf
				Next

				If (Rnd(0.0, 1.0) < prob And temp = False) Then
					e.Events = New Events
					e\name = eventname
					e\room = r
				EndIf
			EndIf
		Next
	EndIf

	Return Null
End Function

Function RemoveEvent(e.Events)
	Local i%
	For i = 0 To EVENT_SOUND_COUNT-1
		If ((e\sounds[i] <> 0)) Then
			FreeSound(e\sounds[i])
		EndIf
	Next

	For i = 0 To EVENT_CHANNEL_COUNT-1
		If ((e\soundChannels[i] <> 0)) Then
			If ((IsChannelPlaying(e\soundChannels[i]))) Then
				StopChannel(e\soundChannels[i])
			EndIf
		EndIf
	Next

	;If ((e\img<>0)) Then FreeImage(e\img)

	Delete e
End Function

Function InitEvents()
	Local e.Events

	CreateEvent("evt_intro", "roomintro", 0)
	CreateEvent("evt_cont_173", "cont_173_1", 0)

	CreateEvent("evt_pd", "pocketdimension", 0)

	;there's a 7% chance that 106 appears in the rooms named "tnnl_plain_2"
	CreateEvent("evt_tunnl_106", "tnnl_plain_2", 0, 0.07 + (0.1*SelectedDifficulty\aggressiveNPCs))

	;the chance for 173 appearing in the first lockroom is about 66%
	;there's a 30% chance that it appears in the later lockrooms
	If (Rand(3)<3) Then CreateEvent("evt_lck_173", "lck_cam_2c", 0)
	CreateEvent("evt_lck_173", "lck_cam_2c", 0, 0.3 + (0.5*SelectedDifficulty\aggressiveNPCs))

	CreateEvent("evt_hll_trick", "hll_plain_2", 0, 0.15)

	CreateEvent("evt_hll_970", "scp_970_2", 0)

	CreateEvent("evt_end_106", "end_1", Rand(0,1))

	CreateEvent("evt_off_l_conf", "off_l_conf_2", 0)

	CreateEvent("evt_hll_fan", "hll_fan_2", 0, 1.0)

	CreateEvent("evt_ele_guard", "hll_ele_2", 0)
	CreateEvent("evt_ele_gonzales", "hll_ele_2", Rand(1,2))

	CreateEvent("evt_strg_939", "strg_939_3", 0, 0)

	CreateEvent("evt_tnnl_smoke", "tnnl_elec_2", 0, 0.2)
	CreateEvent("evt_tnnl_blackout", "tnnl_elec_2", Rand(0,2), 0)
	CreateEvent("evt_tnnl_blackout", "tnnl_elec_2", 0, (0.2*SelectedDifficulty\aggressiveNPCs))

	;173 appears in half of the "lck_tshape_2" -rooms
	CreateEvent("evt_lck_tshape_173", "lck_tshape_2", 0, 0.5 + (0.4*SelectedDifficulty\aggressiveNPCs))

	;the anomalous duck in room2offices2-rooms
	CreateEvent("evt_off_lower", "off_lower_2", 0, 0.7)

	CreateEvent("evt_closets_173", "closets_2", 0)

	CreateEvent("evt_hll_caf", "hll_caf_2", 0)

	CreateEvent("evt_gas_duck", "hll_gas_3", 0)
	CreateEvent("evt_gas_1048", "hll_gas_3", 1)

	;the event that causes the door to open by itself in room2offices3
	CreateEvent("evt_off_2level", "off_2level_2", 0, 1.0)

	CreateEvent("evt_srvr_096", "srvr_096_2", 0)

	CreateEvent("evt_srvr", "srvr_farm_3", 0)
	CreateEvent("evt_srvr", "srvr_lshape_3", 0)

	;the dead guard
	CreateEvent("evt_tnnl_plain_3", "tnnl_plain_3", 0, 0.08)

	CreateEvent("evt_hll_plain_4","hll_plain_4", 0)

	If (Rand(5)<5) Then
		Select Rand(3)
			Case 1
				CreateEvent("evnt_roar_682", "tnnl_plain_2", Rand(0,2), 0)
			Case 2
				CreateEvent("evnt_roar_682", "hll_gas_3", Rand(0,2), 0)
			Case 3
				CreateEvent("evnt_roar_682", "off_plain_2", 0, 0)
		End Select
	EndIf

	CreateEvent("evt_test_173", "test_smallwindow_2", 0, 1.0)

	CreateEvent("evt_tesla", "tsl_ez_2", 0, 0.9)
	CreateEvent("evt_tesla", "tsl_lcz_2", 0, 0.9)
	CreateEvent("evt_tesla", "tsl_hcz_2", 0, 0.9)

	CreateEvent("evt_tnnl_nuke", "tnnl_nuke_2", 0, 0)

	If (Rand(5) < 5) Then
		CreateEvent("evt_cont_895_106", "coffin", 0, 0)
	Else
		CreateEvent("evt_cont_895", "coffin", 0, 0)
	EndIf

	CreateEvent("evt_chck", "chck_lcz_hcz_2", 0, 1.0)
	CreateEvent("evt_chck", "chck_hcz_ez_2", 0, 1.0)

	If (Rand(2)=1) Then
		CreateEvent("evt_hll_106_victim", "hll_plain_3", Rand(1,2))
		CreateEvent("evt_hll_sinkhole", "hll_plain_3", Rand(2,3))
	Else
		CreateEvent("evt_hll_106_victim", "hll_pipes_3", Rand(1,2))
		CreateEvent("evt_hll_sinkhole", "hll_pipes_3", Rand(2,3))
	EndIf
	CreateEvent("evt_hll_sinkhole", "hll_plain_4", Rand(1,2))

	CreateEvent("evt_cont_079", "cont_079_1", 0, 0)

	CreateEvent("evt_cont_049", "cont_049_2", 0, 0)

	CreateEvent("evt_cont_012", "cont_012_2", 0, 0)

	CreateEvent("evt_cont_035", "cont_035_1", 0, 0)

	CreateEvent("evt_cont_008", "cont_008_1", 0, 0)

	CreateEvent("evt_cont_106", "cont_106_1", 0, 0)

	CreateEvent("evt_cont_914", "cont_914_1", 0, 0)

	CreateEvent("evt_toilets_suicide", "hll_toilets_2", 1)
	CreateEvent("evt_toilets_789j", "hll_toilets_2", 0, 0.8)

	CreateEvent("evt_106_pass", "tnnl_pipes_2", Rand(0, 3))

	CreateEvent("evt_hll_gas", "hll_gas_2", 0, 0.4 + (0.4*SelectedDifficulty\aggressiveNPCs))
	CreateEvent("evt_hll_gas", "hll_lshape_2",0,0.4 + (0.4*SelectedDifficulty\aggressiveNPCs))

	CreateEvent("evt_test_682", "test_682_2", 0)

	CreateEvent("evt_tnnl_maintenance", "tnnl_maintenance_2", 0)

	CreateEvent("evt_strg_elec", "strg_elec_2c", 0)

	CreateEvent("evt_gatea", "exit_gatea_1", 0)
	CreateEvent("evt_extend_gatea", "extend_gatea_1", 0)
	CreateEvent("evt_gateb", "exit_gateb_1", 0)

	CreateEvent("evt_cont_205", "cont_205_1", 0)

	CreateEvent("evt_test_860","test_860_2", 0)

	CreateEvent("evt_cont_966","cont_966_3", 0)

	CreateEvent("cont_1123_2", "cont_1123_2", 0, 0)

	CreateEvent("evt_1499", "dimension1499",0)
	CreateEvent("evt_cont_1162", "cont_1162_2c",0)
	CreateEvent("evt_cont_500_1499", "cont_500_1499_2",0)

	CreateEvent("evt_tnnl_plain_4", "tnnl_plain_4",0)

	CreateEvent("evt_lck_air", "lck_air_2",0,1.0)
	CreateEvent("evt_lck_air", "lck_ez_3",0,1.0)
	CreateEvent("evt_lck_air_broke", "lck_air_broke_2",Rand(0,1))

	CreateEvent("evt_hll_sl", "hll_sl_2",0)
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
		; Does the event have music to play?
		If ((e\overwriteMusic)) Then
			SetNextMusicTrack(e\musicTrack)
		ElseIf ((Not musicManager\useDefault)) Then
			; If the event was previously playing music then go back to the default.
			If ((e\musicTrack = musicManager\nowPlaying)) Then
				RestoreDefaultMusic()
			EndIf
		EndIf

		Select e\name
			Case "evt_intro"
				UpdateEvent173(e)
			Case "evt_cont_173"
				UpdateEventAlarm(e)
			Case "evt_pd"
				UpdateEvent_pocketdimension(e)
			Case "evt_tunnl_106"
				UpdateEventTunnel106(e)
			Case "evt_lck_173"
				UpdateEventLockroom173(e)
			Case "evt_hll_trick"
				UpdateEventRoom2trick(e)
			Case "evt_hll_970"
				UpdateEvent_scp_970_2(e)
			Case "evt_end_106"
				UpdateEventEndroom106(e)
			Case "evt_off_l_conf"
				UpdateEvent_off_l_conf_2(e)
			Case "evt_hll_fan"
				UpdateEventRoom2fan(e)
			Case "evt_ele_guard"
				UpdateEvent_hll_ele_2(e)
			Case "evt_ele_gonzales"
				UpdateEventRoom2elevator2(e)
			Case "evt_strg_939"
				UpdateEvent_strg_939_3(e)
			Case "evt_tnnl_smoke"
				UpdateEventTunnel2smoke(e)
			Case "evt_tnnl_blackout"
				UpdateEvent_tnnl_elec_2(e)
			Case "evt_lck_tshape_173"
				UpdateEventRoom2doors173(e)
			Case "evt_off_lower"
				UpdateEvent_off_lower_2(e)
			Case "evt_closets_173"
				UpdateEvent_closets_2(e)
			Case "evt_hll_caf"
				UpdateEvent_hll_caf_2(e)
			Case "evt_gas_duck"
				UpdateEventRoom3pitduck(e)
			Case "evt_gas_1048"
				UpdateEventRoom3pit1048(e)
			Case "evt_off_2level"
				UpdateEvent_off_2level_2(e)
			Case "evt_srvr_096"
				UpdateEvent_srvr_096_2(e)
			Case "evt_srvr"
				UpdateEvent_srvr_farm_3(e)
			Case "evt_tnnl_plain_3"
				UpdateEvent_tnnl_plain_3(e)
			Case "evt_hll_plain_4"
				UpdateEvent_hll_plain_4(e)
			Case "evt_roar_682"
				UpdateEvent682roar(e)
			Case "evt_test_173"
				UpdateEventTestroom173(e)
			Case "evt_tesla"
				UpdateEvent_tesla(e)
			Case "evt_tnnl_nuke"
				UpdateEvent_tnnl_nuke_2(e)
			Case "evt_cont_895_106"
				UpdateEventCoffin106(e)
			Case "evt_cont_895"
				UpdateEventCoffin(e)
			Case "evt_chck"
				UpdateEventCheckpoint(e)
			Case "evt_hll_106_victim"
				UpdateEvent106victim(e)
			Case "evt_hll_sinkhole"
				UpdateEvent106sinkhole(e)
			Case "evt_cont_079"
				UpdateEvent_cont_079_1(e)
			Case "evt_cont_049"
				UpdateEvent_cont_049_2(e)
			Case "evt_cont_012"
				UpdateEvent_cont_012_2(e)
			Case "evt_cont_035"
				UpdateEvent_cont_035_1(e)
			Case "evt_cont_008"
				UpdateEvent_cont_008_1(e)
			Case "evt_cont_106"
				UpdateEvent_cont_106_1(e)
			Case "evt_cont_914"
				UpdateEvent_cont_914_1(e)
			Case "evt_toilets_suicide"
				UpdateEventToiletguard(e)
			Case "evt_toilets_789j"
				UpdateEventButtghost(e)
			Case "evt_106_pass"
				UpdateEventRoom2pipes106(e)
			Case "evt_hll_gas"
				UpdateEvent_hll_gas_2(e)
			Case "evt_test_682"
				UpdateEvent_test_682_2(e)
			Case "evt_tnnl_maintenance"
				UpdateEvent_tnnl_maintenance_2(e)
			Case "evt_strg_elec"
				UpdateEvent_strg_elec_2c(e)
			Case "evt_gatea"
				UpdateEvent_exit_gatea_1(e)
			Case "evt_extend_gatea"
				UpdateEvent_extend_gatea_1(e)
			Case "evt_gateb"
				UpdateEventExit1(e)
			Case "evt_cont_205"
				UpdateEvent_cont_205_1(e)
			Case "evt_test_860"
				UpdateEvent_test_860_2(e)
			Case "evt_cont_966"
				UpdateEvent_cont_966_3(e)
			Case "evt_cont_1123"
				UpdateEvent_cont_1123_2(e)
			Case "evt_1499"
				UpdateEvent_dimension1499(e)
			Case "evt_cont_1162"
				UpdateEvent_cont_1162_2c(e)
			Case "evt_cont_500_1499"
				UpdateEvent_cont_500_1499_2(e)
			Case "evt_tnnl_plain_4"
				UpdateEvent_tnnl_plain_4(e)
			Case "evt_lck_air"
				UpdateEventRoom_gw(e)
			Case "evt_lck_air_broke"
				UpdateEventRoom2gw_b(e)
			Case "evt_hll_sl"
				UpdateEvent_hll_sl_2(e)
		End Select
	Next

	If (ExplosionTimer > 0) Then
		ExplosionTimer = ExplosionTimer+timing\tickDuration

		If (ExplosionTimer < 140.0) Then
			If (ExplosionTimer-timing\tickDuration < 5.0) Then
				PlaySound2(LoadTempSound("SFX/Ending/GateB/Nuke1.ogg"))
				mainPlayer\camShake = 10.0
				ExplosionTimer = 5.0
			EndIf

			mainPlayer\camShake = CurveValue(ExplosionTimer/60.0,mainPlayer\camShake, 50.0)
		Else
			mainPlayer\camShake = Min((ExplosionTimer/20.0),20.0)
			If (ExplosionTimer-timing\tickDuration < 140.0) Then
				mainPlayer\blinkTimer = 1.0
				PlaySound2(LoadTempSound("SFX/Ending/GateB/Nuke2.ogg"))
				For i = 0 To 40
					p.Particles = CreateParticle(EntityX(mainPlayer\collider)+Rnd(-0.5,0.5),EntityY(mainPlayer\collider)-Rnd(0.2,1.5),EntityZ(mainPlayer\collider)+Rnd(-0.5,0.5),0, Rnd(0.2,0.6), 0.0, 350)
					RotateEntity(p\pvt,-90,0,0,True)
					p\speed = Rnd(0.05,0.07)
				Next
			EndIf
			mainPlayer\lightFlash = Min((ExplosionTimer-160.0)/40.0,2.0)
			If (ExplosionTimer > 160) Then Kill(mainPlayer); : EndingTimer = Min(mainPlayer\fallTimer,-0.1)
			If (ExplosionTimer > 500) Then ExplosionTimer = 0
		EndIf

	EndIf
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D