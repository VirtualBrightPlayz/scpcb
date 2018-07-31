Include "SourceCode/Rooms/Room_closets_2.bb"
Include "SourceCode/Rooms/Room_cont_008_1.bb"
Include "SourceCode/Rooms/Room_cont_012_2.bb"
Include "SourceCode/Rooms/Room_cont_035_1.bb"
Include "SourceCode/Rooms/Room_cont_049_2.bb"
Include "SourceCode/Rooms/Room_cont_079_1.bb"
Include "SourceCode/Rooms/Room_cont_106_1.bb"
Include "SourceCode/Rooms/Room_cont_1123_2.bb"
Include "SourceCode/Rooms/Room_cont_1162_2c.bb"
Include "SourceCode/Rooms/Room_cont_173_1.bb"
Include "SourceCode/Rooms/Room_cont_205_1.bb"
Include "SourceCode/Rooms/Room_cont_500_1499_2.bb"
Include "SourceCode/Rooms/Room_cont_513_3.bb"
Include "SourceCode/Rooms/Room_cont_714_860_1025_2.bb"
Include "SourceCode/Rooms/Room_cont_895_1.bb"
Include "SourceCode/Rooms/Room_cont_914_1.bb"
Include "SourceCode/Rooms/Room_cont_966_3.bb"
Include "SourceCode/Rooms/Room_dimension1499.bb"
Include "SourceCode/Rooms/Room_end_1.bb"
Include "SourceCode/Rooms/Room_exit_gatea_1.bb"
Include "SourceCode/Rooms/Room_exit_gateb_1.bb"
Include "SourceCode/Rooms/Room_extend_gatea_1.bb"
Include "SourceCode/Rooms/Room_hll_bench_3.bb"
Include "SourceCode/Rooms/Room_hll_caf_2.bb"
Include "SourceCode/Rooms/Room_hll_dirty_3.bb"
Include "SourceCode/Rooms/Room_hll_ele_2.bb"
Include "SourceCode/Rooms/Room_hll_fan_2.bb"
Include "SourceCode/Rooms/Room_hll_gas_2.bb"
Include "SourceCode/Rooms/Room_hll_gas_3.bb"
Include "SourceCode/Rooms/Room_hll_lshape_2.bb"
Include "SourceCode/Rooms/Room_hll_pipes_3.bb"
Include "SourceCode/Rooms/Room_hll_plain_2.bb"
Include "SourceCode/Rooms/Room_hll_plain_3.bb"
Include "SourceCode/Rooms/Room_hll_plain_4.bb"
Include "SourceCode/Rooms/Room_hll_sl_2.bb"
Include "SourceCode/Rooms/Room_hll_toilets_2.bb"
Include "SourceCode/Rooms/Room_intro.bb"
Include "SourceCode/Rooms/Room_lck_096_2c.bb"
Include "SourceCode/Rooms/Room_lck_air_2.bb"
Include "SourceCode/Rooms/Room_lck_broke_2c.bb"
Include "SourceCode/Rooms/Room_lck_cam_2c.bb"
Include "SourceCode/Rooms/Room_lck_ez_3.bb"
Include "SourceCode/Rooms/Room_lck_tshape_2.bb"
Include "SourceCode/Rooms/Room_lifts_1.bb"
Include "SourceCode/Rooms/Room_off_2level_2.bb"
Include "SourceCode/Rooms/Room_off_bain_2.bb"
Include "SourceCode/Rooms/Room_off_gears_may_har_2.bb"
Include "SourceCode/Rooms/Room_off_glss_3.bb"
Include "SourceCode/Rooms/Room_off_lower_2.bb"
Include "SourceCode/Rooms/Room_off_l_conf_2.bb"
Include "SourceCode/Rooms/Room_off_plain_2.bb"
Include "SourceCode/Rooms/Room_off_rosewood_2.bb"
Include "SourceCode/Rooms/Room_pocketdimension.bb"
Include "SourceCode/Rooms/Room_scp_970_2.bb"
Include "SourceCode/Rooms/Room_srvr_096_2.bb"
Include "SourceCode/Rooms/Room_srvr_farm_3.bb"
Include "SourceCode/Rooms/Room_srvr_lshape_3.bb"
Include "SourceCode/Rooms/Room_srvr_pc_2.bb"
Include "SourceCode/Rooms/Room_strg_939_3.bb"
Include "SourceCode/Rooms/Room_strg_elec_2c.bb"
Include "SourceCode/Rooms/Room_test_682_2.bb"
Include "SourceCode/Rooms/Room_test_860_2.bb"
Include "SourceCode/Rooms/Room_test_smallwindow_2.bb"
Include "SourceCode/Rooms/Room_tnnl_elec_2.bb"
Include "SourceCode/Rooms/Room_tnnl_maintenance_2.bb"
Include "SourceCode/Rooms/Room_tnnl_pipes_2.bb"
Include "SourceCode/Rooms/Room_tnnl_plain_2.bb"
Include "SourceCode/Rooms/Room_tnnl_plain_3.bb"
Include "SourceCode/Rooms/Room_tnnl_plain_4.bb"
Include "SourceCode/Rooms/Room_hll_tsl.bb"
Include "SourceCode/Rooms/Room_tnnl_nuke_2.bb"

Include "SourceCode/RM2.bb"

Include "SourceCode/Skybox.bb"

Include "SourceCode/Materials.bb"

Type Material
	Field name$
	Field diff%
	;Field Bump

	Field stepSound%
End Type

Function LoadMaterials(file$)
	Local TemporaryString$
	Local mat.Material = Null
	Local StrTemp$ = ""

	Local f% = OpenFile(file)

	Local stepSound$ = ""

	While (Not Eof(f))
		TemporaryString = Trim(ReadLine(f))
		If (Left(TemporaryString,1) = "[") Then
			TemporaryString = Mid(TemporaryString, 2, Len(TemporaryString) - 2)

			mat = New Material

			mat\name = Lower(TemporaryString)

			mat\diff = 0

			stepSound = GetINIString(file, TemporaryString, "stepsound")
			If (Lower(stepSound)="metal") Then mat\stepSound = STEPSOUND_METAL
		EndIf
	Wend

	CloseFile(f)

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

	While (Instr(s,char+char))
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

Const MaxRoomLights% = 32
Const MaxRoomEmitters% = 8
Const MaxRoomObjects% = 30

Const ROOM0%=0, ROOM1% = 1, ROOM2% = 2, ROOM2C% = 3, ROOM3% = 4, ROOM4% = 5

Const ZONE_LCZ% = 1, ZONE_HCZ% = 2, ZONE_EZ% = 4

Type RoomTemplate
	Field name$
	Field shape%
	;TODO: might not be needed?
	Field large%

	Field objPath$
	Field loaded%

	Field opaqueMesh%
	Field alphaMesh%
	Field collisionObjs.IntArrayList
	Field props.IntArrayList

	Field zones%

	;TODO: cleanup?
	Field tempSoundEmitter%[MaxRoomEmitters]
	Field tempSoundEmitterX#[MaxRoomEmitters],tempSoundEmitterY#[MaxRoomEmitters],tempSoundEmitterZ#[MaxRoomEmitters]
	Field tempSoundEmitterRange#[MaxRoomEmitters]

	Field commonness#
	Field minAmount%, maxAmount%
	Field xRangeStart#, xRangeEnd#
	Field yRangeStart#, yrangeEnd#
	Field disableDecals%

	;TODO: remove
	Field tempTriggerboxAmount%
	Field tempTriggerbox%[128]
	Field tempTriggerboxName$[128]
End Type

Function CreateRoomTemplate.RoomTemplate(meshpath$)
	Local rt.RoomTemplate = New RoomTemplate

	rt\objPath = meshpath
	rt\loaded = False

	Return rt
End Function

Function LoadRoomTemplates(file$)
	Local TemporaryString$, i%
	Local rt.RoomTemplate = Null
	Local StrTemp$ = ""
	Local Zones$ = ""
	Local AmountRange$ = ""
	Local xRange$, yRange$

	Local f% = OpenFile(file)

	While (Not Eof(f))
		TemporaryString = Trim(ReadLine(f))
		If (Left(TemporaryString,1) = "[") Then
			DebugLog(TemporaryString)
			TemporaryString = Mid(TemporaryString, 2, Len(TemporaryString) - 2)
			If (Not (Lower(TemporaryString)="room ambience")) Then
				StrTemp = GetINIString(file, TemporaryString, "meshpath")

				rt = CreateRoomTemplate(StrTemp)
				rt\name = Lower(TemporaryString)

				StrTemp = Lower(GetINIString(file, TemporaryString, "shape", "0"))

				Select (StrTemp)
					Case "room0", "0"
						rt\shape = ROOM0
					Case "room1", "1"
						rt\shape = ROOM1
					Case "room2", "2"
						rt\shape = ROOM2
					Case "room2c", "2c"
						rt\shape = ROOM2C
					Case "room3", "3"
						rt\shape = ROOM3
					Case "room4", "4"
						rt\shape = ROOM4
					Default
						rt\shape = ROOM0
				End Select

				Zones = Lower(GetINIString(file, TemporaryString, "zones"))
				rt\zones = 0
				If (Instr(Zones,"lcz")>0) Then
					rt\zones = rt\zones Or ZONE_LCZ
				EndIf
				If (Instr(Zones,"hcz")>0) Then
					rt\zones = rt\zones Or ZONE_HCZ
				EndIf
				If (Instr(Zones,"ez")>0) Then
					rt\zones = rt\zones Or ZONE_EZ
				EndIf

				If (rt\shape<>ROOM0) Then
					rt\commonness = Max(Min(GetINIFloat(file, TemporaryString, "commonness"), 100), 0)

					AmountRange = GetINIString(file, TemporaryString, "amount", "")
					If (AmountRange="") Then
						rt\minAmount = -1
						rt\maxAmount = -1
					ElseIf ((Instr(AmountRange,"-")>0)) Then
						rt\minAmount = Int(Left(AmountRange,Instr(AmountRange,"-")))
						rt\maxAmount = Int(Mid(AmountRange,Instr(AmountRange,"-")+1))
					Else
						rt\minAmount = Int(AmountRange)
						rt\maxAmount = rt\minAmount
					EndIf

					rt\large = GetINIInt(file, TemporaryString, "large")
					rt\disableDecals = GetINIInt(file, TemporaryString, "disabledecals")

					xRange = GetINIString(file, TemporaryString, "xrange")
					yRange = GetINIString(file, TemporaryString, "yrange")

					If (xRange = "") Then
						xRange = "0-1"
					EndIf
					If (yRange = "") Then
						yRange = "0-1"
					EndIf

					rt\xRangeStart = Float(Left(xRange,Instr(xRange,"-")))
					rt\xRangeEnd = Float(Mid(xRange,Instr(xRange,"-")+1))

					rt\yRangeStart = Float(Left(yRange,Instr(yRange,"-")))
					rt\yrangeEnd = Float(Mid(yRange,Instr(yRange,"-")+1))
				Else
					rt\minAmount = 0
					rt\maxAmount = 0
					rt\zones = 0
					rt\commonness = 0
				EndIf

			EndIf
		EndIf
	Wend

	i = 1
	Repeat
		StrTemp = GetINIString(file, "room ambience", "ambience"+Str(i))
		If (StrTemp = "") Then Exit

		RoomAmbience[i]=LoadSound(StrTemp)
		i=i+1
	Forever

	CloseFile(f)

End Function

Function LoadRoomMesh(rt.RoomTemplate)
	LoadRM2(rt)
End Function

Const MAP_SIZE% = 19
Global RoomScale# = 8.0 / 2048.0

Global RoomAmbience%[20]

Global Sky%

Global HideDistance# = 15.0

;TODO: remove/replace with functions
Global SecondaryLightOn#
Global RemoteDoorOn%
Global Contained106%

Type Room
	Field zone%

	Field found%

	Field x#, y#, z#
	Field angle%
	Field roomTemplate.RoomTemplate

	;TODO: rename
	Field obj%
	Field opaqueMesh%
	Field alphaMesh%
	Field collisionObjs.IntArray
	Field props.IntArray

	Field dist#

	Field soundCHN%

	Field fr.Forest

	;TODO: use arraylists for all this stuff?
	Field soundEmitter%[MaxRoomEmitters]
	Field soundEmitterObj%[MaxRoomEmitters]
	Field soundEmitterRange#[MaxRoomEmitters]
	Field soundEmitterCHN%[MaxRoomEmitters]

	Field lights%[MaxRoomLights]
	Field lightIntensity#[MaxRoomLights]

	Field lightSprites%[MaxRoomLights]

	Field objects%[MaxRoomObjects]
	Field levers.Lever[11]
	Field doors.Door[7]
	Field npc.NPC[12]
	Field grid.Grid

	Field adjacent.Room[4]
	Field adjDoor.Door[4]

	;TODO: what the fuck
	Field lightSpritesPivot%[MaxRoomLights]
	Field triggerboxAmount%
	Field triggerbox%[128]
	Field triggerboxName$[128]
End Type

Const gridsz%=20
Type Grid
	Field grid%[gridsz*gridsz]
	Field angles%[gridsz*gridsz]
	Field meshes%[7]
	Field entities%[gridsz*gridsz]
	Field waypoints.WayPoint[gridsz*gridsz]
End Type

Function UpdateGrid(grid.Grid)
	;local variables
	Local tx%,ty%
	For tx = 0 To gridsz-1
		For ty = 0 To gridsz-1
			If (grid\entities[tx+(ty*gridsz)]<>0) Then
				If (Abs(EntityY(mainPlayer\collider,True)-EntityY(grid\entities[tx+(ty*gridsz)],True))>4.0) Then Exit
				If (Abs(EntityX(mainPlayer\collider,True)-EntityX(grid\entities[tx+(ty*gridsz)],True))<HideDistance) Then
					If (Abs(EntityZ(mainPlayer\collider,True)-EntityZ(grid\entities[tx+(ty*gridsz)],True))<HideDistance) Then
						ShowEntity(grid\entities[tx+(ty*gridsz)])
					Else
						HideEntity(grid\entities[tx+(ty*gridsz)])
					EndIf
				Else
					HideEntity(grid\entities[tx+(ty*gridsz)])
				EndIf
			EndIf
		Next
	Next
End Function

Function GetRoomTemplate.RoomTemplate(name$)
	name = Lower(name)

	Local rt.RoomTemplate
	For rt = Each RoomTemplate
		If (rt\name = name) Then
			Return rt
		EndIf
	Next
End Function

Function CountRooms%(rt.RoomTemplate)
	Local count% = 0

	Local r.Room
	For r = Each Room
		If (r\roomTemplate = rt) Then count=count+1
	Next
	Return count
End Function

Function CreateRoom.Room(rt.RoomTemplate, x#, y#, z#)
	Local r.Room = New Room

	DebugLog("Placing "+rt\name)

	; TODO: do we still need this?
	r\zone = rt\zones
	r\x = x : r\y = y : r\z = z

	r\roomTemplate = rt

	If (Not rt\loaded) Then LoadRoomMesh(rt)

	Local tempObj%
	Local tempProp.Prop

	r\obj = CreatePivot()
	r\opaqueMesh = CopyEntity(rt\opaqueMesh) : ScaleEntity(r\opaqueMesh, RoomScale, RoomScale, RoomScale)
	EntityParent(r\opaqueMesh,r\obj)
	ShowEntity(r\opaqueMesh)
	If (rt\alphaMesh<>0) Then
		r\alphaMesh = CopyEntity(rt\alphaMesh) : ScaleEntity(r\alphaMesh, RoomScale, RoomScale, RoomScale)
		ShowEntity(r\alphaMesh)
		EntityParent(r\alphaMesh,r\obj)
	EndIf
	r\collisionObjs = CreateIntArray(rt\collisionObjs\size)
	Local i%
	For i = 0 To rt\collisionObjs\size-1
		tempObj = CopyEntity(GetIntArrayListElem(rt\collisionObjs,i)) : ScaleEntity(tempObj, RoomScale, RoomScale, RoomScale)
		SetIntArrayElem(r\collisionObjs,tempObj,i)
		ShowEntity(tempObj) : EntityAlpha(tempObj,0.0)
		EntityParent(tempObj,r\obj)
	Next
	If (rt\props<>Null) Then
		r\props = CreateIntArray(rt\props\size)
		For i = 0 To rt\props\size-1
			tempProp = Object.Prop(GetIntArrayListElem(rt\props,i))
			tempObj = CopyEntity(tempProp\obj)
			SetIntArrayElem(r\props,tempObj,i)
			PositionEntity(tempObj,tempProp\x*RoomScale,tempProp\y*RoomScale,tempProp\z*RoomScale)
			RotateEntity(tempObj,tempProp\pitch,tempProp\yaw,tempProp\roll)
			ScaleEntity(tempObj,tempProp\xScale*RoomScale,tempProp\yScale*RoomScale,tempProp\zScale*RoomScale)
			ShowEntity(tempObj)
			EntityParent(tempObj,r\obj)
		Next
	EndIf

	PositionEntity(r\obj, x, y, z)
	FillRoom(r)

	Return r
End Function

Function FillRoom(r.Room)
	Select (r\roomTemplate\name)
		Case "test_860_2"
			FillRoom_test_860_2(r)
		Case "lck_cam_2c"
			FillRoom_lck_cam_2c(r)
		Case "lck_096_2c"
			FillRoom_lck_096_2c(r)
		Case "extend_gatea_1"
			FillRoom_extend_gatea_1(r)
		Case "exit_gatea_1"
			FillRoom_exit_gatea_1(r)
		Case "exit1"
			FillRoom_exit_gateb_1(r)
		Case "cont_079_1"
			FillRoom_cont_079_1(r)
		Case "hll_gas_2"
			FillRoom_hll_gas_2(r)
		Case "test_smallwindow_2"
			FillRoom_test_smallwindow_2(r)
		Case "tnnl_plain_3"
			FillRoom_tnnl_plain_3(r)
		Case "hll_toilets_2"
			FillRoom_hll_toilets_2(r)
		Case "scp_970_2"
			FillRoom_scp_970_2(r)
		Case "off_rosewood_2"
			FillRoom_off_rosewood_2(r)
		Case "off_gears_may_har_2"
			FillRoom_off_gears_may_har_2(r)
		Case "off_l_conf_2"
			FillRoom_off_l_conf_2(r)
		Case "hll_ele_2"
			FillRoom_hll_ele_2(r)
		Case "hll_caf_2"
			FillRoom_hll_caf_2(r)
		Case "tnnl_nuke_2"
			FillRoom_tnnl_nuke_2(r)
		Case "tnnl_maintenance_2"
			FillRoom_tnnl_maintenance_2(r)
		Case "cont_008_1"
			FillRoom_cont_008_1(r)
		Case "cont_035_1"
			FillRoom_cont_035_1(r)
		Case "cont_513_3"
			FillRoom_cont_513_3(r)
		Case "cont_966_3"
			FillRoom_cont_966_3(r)
		Case "strg_939_3"
			FillRoom_strg_939_3(r)
		Case "cont_049_2"
			FillRoom_cont_049_2(r)
		Case "hll_fan_2"
			FillRoom_hll_fan_2(r)
		Case "cont_012_2"
			FillRoom_cont_012_2(r)
		Case "tnnl_elec_2"
			FillRoom_tnnl_elec_2(r)
		Case "tnnl_pipes_2"
			FillRoom_tnnl_pipes_2(r)
		Case "hll_gas_3"
			FillRoom_hll_gas_3(r)
		Case "srvr_096_2"
			FillRoom_srvr_096_2(r)
		Case "srvr_farm_3"
			FillRoom_srvr_farm_3(r)
		Case "srvr_lshape_3"
			FillRoom_srvr_lshape_3(r)
		Case "test_682_2"
			FillRoom_test_682_2(r)
		Case "closets_2"
			FillRoom_closets_2(r)
		Case "off_plain_2"
			FillRoom_off_plain_2(r)
		Case "off_lower_2"
			FillRoom_off_lower_2(r)
		Case "off_2level_2"
			FillRoom_off_2level_2(r)
		Case "cont_173_1"
			FillRoom_cont_173_1(r)
		Case "cont_714_860_1025_2"
			FillRoom_cont_714_860_1025_2(r)
		Case "cont_205_1"
			FillRoom_cont_205_1(r)
		Case "end_1"
			FillRoom_end_1(r)
		Case "coffin"
			FillRoom_cont_895_1(r)
		Case "tsl_ez_2","tsl_lcz_2","tsl_hcz_2"
			FillRoom_hll_tsl(r)
		Case "lck_tshape_2"
			FillRoom_lck_tshape_2(r)
		Case "cont_914_1"
			FillRoom_cont_914_1(r)
		Case "roomintro"
			FillRoom_intro(r)
		Case "strg_elec_2c"
			FillRoom_strg_elec_2c(r)
		Case "cont_106_1"
			FillRoom_cont_106_1(r)
		Case "cont_1123_2"
			FillRoom_cont_1123_2(r)
		Case "pocketdimension"
			FillRoom_pocketdimension(r)
		Case "hll_bench_3"
			FillRoom_hll_bench_3(r)
		Case "lifts_1"
			FillRoom_lifts_1(r)
		Case "srvr_pc_2"
			FillRoom_srvr_pc_2(r)
		Case "lck_air_2","lck_air_broke_2"
		    FillRoom_lck_air_2(r)
		Case "lck_ez_3"
	        FillRoom_lck_ez_3(r)
		Case "cont_1162_2c"
			FillRoom_cont_1162_2c(r)
		Case "cont_500_1499_2"
			FillRoom_cont_500_1499_2(r)
		Case "off_glss_3"
			FillRoom_off_glss_3(r)
		Case "off_bain_2"
			FillRoom_off_bain_2(r)
		Case "hll_sl_2"
			FillRoom_hll_sl_2(r)
		Case "hll_lshape_2"
			FillRoom_hll_lshape_2(r)
		Case "hll_dirty_3"
			FillRoom_hll_dirty_3(r)
		Case "lck_broke_2c"
			FillRoom_lck_broke_2c(r)
	End Select

	Local lt.LightTemplate, newlt%
	For lt = Each LightTemplate
		If (lt\roomtemplate = r\roomTemplate) Then
			newlt = AddLight(r, r\x+lt\x*RoomScale, r\y+lt\y*RoomScale, r\z+lt\z*RoomScale, lt\ltype, lt\range, lt\r, lt\g, lt\b)
			If (newlt <> 0) Then
				If (lt\ltype = 3) Then
					LightConeAngles(newlt, lt\innerconeangle, lt\outerconeangle)
					RotateEntity(newlt, lt\pitch, lt\yaw, 0)
				EndIf
			EndIf
		EndIf
	Next

	Local ts.TempScreen
	For ts = Each TempScreen
		If (ts\roomtemplate = r\roomTemplate) Then
			CreateScreen(r\x+ts\x*RoomScale, r\y+ts\y*RoomScale, r\z+ts\z*RoomScale, ts\imgpath, r)
		EndIf
	Next

	Local waypoints.IntArrayList = CreateIntArrayList()
	Local waypoint.WayPoint
	Local tw.TempWayPoint
	For tw = Each TempWayPoint
		If (tw\roomtemplate = r\roomTemplate) Then
			waypoint = CreateWaypoint(r\x+tw\x*RoomScale, r\y+tw\y*RoomScale, r\z+tw\z*RoomScale, r)
			PushIntArrayListElem(waypoints,Handle(waypoint))
		EndIf
	Next

	Local i% = 0, j%
	For tw = Each TempWayPoint
		If (tw\roomtemplate = r\roomTemplate) Then
			waypoint = Object.WayPoint(GetIntArrayListElem(waypoints,i))
			For j = 0 To 15
				If (tw\connectedTo[j]=0) Then Exit
				waypoint\connected[j] = Object.WayPoint(GetIntArrayListElem(waypoints,tw\connectedTo[j]-1))
				waypoint\dist[j] = EntityDistance(waypoint\obj,waypoint\connected[j]\obj)
			Next
			i=i+1
		EndIf
	Next

	DeleteIntArrayList(waypoints)

;	If r\roomTemplate\tempTriggerboxAmount > 0
;		r\triggerboxAmount = r\roomTemplate\tempTriggerboxAmount
;		For i = 0 To r\triggerboxAmount-1
;			r\triggerbox[i] = CopyEntity(r\roomTemplate\tempTriggerbox[i],r\obj)
;			EntityAlpha(r\triggerbox[i],0.0)
;			r\triggerboxName[i] = r\roomTemplate\tempTriggerboxName[i]
;			DebugLog("Triggerbox found: "+i)
;			DebugLog("Triggerbox "+i+" name: "+r\triggerboxName[i])
;		Next
;	EndIf

	For i = 0 To MaxRoomEmitters-1
		If (r\roomTemplate\tempSoundEmitter[i]<>0) Then
			r\soundEmitterObj[i]=CreatePivot(r\obj)
			PositionEntity(r\soundEmitterObj[i], r\x+r\roomTemplate\tempSoundEmitterX[i]*RoomScale,r\y+r\roomTemplate\tempSoundEmitterY[i]*RoomScale,r\z+r\roomTemplate\tempSoundEmitterZ[i]*RoomScale,True)
			EntityParent(r\soundEmitterObj[i],r\obj)

			r\soundEmitter[i] = r\roomTemplate\tempSoundEmitter[i]
			r\soundEmitterRange[i] = r\roomTemplate\tempSoundEmitterRange[i]
		EndIf
	Next

End Function

Function SetRoomVisibility(r.Room,on%)
	If (on) Then
		ShowEntity(r\opaqueMesh)
		If (r\alphaMesh<>0) Then
			ShowEntity(r\alphaMesh)
		EndIf
	Else
		HideEntity(r\opaqueMesh)
		If (r\alphaMesh<>0) Then
			HideEntity(r\alphaMesh)
		EndIf
	EndIf
End Function

Function UpdateRooms()
	Local dist#, i%, j%, r.Room, minDist#

	Local x#,z#,hide%=True

	;PlayerZone=Min(Max(GetZone(EntityZ(mainPlayer\collider)/8.0),0),ZONEAMOUNT-1)

	;TempLightVolume=0
	Local foundPlayerRoom% = False
	If (mainPlayer\currRoom<>Null) Then
		If (Abs(EntityY(mainPlayer\collider) - EntityY(mainPlayer\currRoom\obj)) < 1.5) Then
			x = Abs(mainPlayer\currRoom\x-EntityX(mainPlayer\collider,True))
			If (x < 4.0) Then
				z = Abs(mainPlayer\currRoom\z-EntityZ(mainPlayer\collider,True))
				If (z < 4.0) Then
					foundPlayerRoom = True
				EndIf
			EndIf

			If (foundPlayerRoom = False) Then ;it's likely that an adjacent room is the new player room, check for that
				For i=0 To 3
					If (mainPlayer\currRoom\adjacent[i]<>Null) Then
						x = Abs(mainPlayer\currRoom\adjacent[i]\x-EntityX(mainPlayer\collider,True))
						If (x < 4.0) Then
							z = Abs(mainPlayer\currRoom\adjacent[i]\z-EntityZ(mainPlayer\collider,True))
							If (z < 4.0) Then
								foundPlayerRoom = True
								mainPlayer\currRoom = mainPlayer\currRoom\adjacent[i]
								Exit
							EndIf
						EndIf
					EndIf
				Next
			EndIf
		Else
			foundPlayerRoom = True ;mainPlayer\currRoom stays the same when you're high up, or deep down
		EndIf
	Else
		minDist = 999.0
		For r = Each Room
			x = Abs(r\x-EntityX(mainPlayer\collider,True))
			z = Abs(r\z-EntityZ(mainPlayer\collider,True))
			r\dist = Max(x,z)

			If (r\dist<minDist) Then
				mainPlayer\currRoom = r
				minDist = r\dist
			EndIf
		Next
		Return
	EndIf

	For r = Each Room
		x = Abs(r\x-EntityX(mainPlayer\collider,True))
		z = Abs(r\z-EntityZ(mainPlayer\collider,True))
		r\dist = Max(x,z)


		If (x<16 And z < 16) Then
			For i = 0 To MaxRoomEmitters-1
				If (r\soundEmitter[i]<>0) Then
					dist = EntityDistance(r\soundEmitterObj[i],mainPlayer\collider)
					If (dist < r\soundEmitterRange[i]) Then
						r\soundEmitterCHN[i] = LoopRangedSound(RoomAmbience[r\soundEmitter[i]],r\soundEmitterCHN[i], mainPlayer\cam, r\soundEmitterObj[i],r\soundEmitterRange[i])
					EndIf
				EndIf
			Next

			If (Not foundPlayerRoom) And (mainPlayer\currRoom<>r) Then
				If (x < 4.0) Then
					If (z < 4.0) Then
						If (Abs(EntityY(mainPlayer\collider) - EntityY(r\obj)) < 1.5) Then mainPlayer\currRoom = r
						foundPlayerRoom = True
					EndIf
				EndIf
			EndIf
		EndIf

		hide = True

		If (r=mainPlayer\currRoom) Then hide = False
		If (hide) Then
			If (IsRoomAdjacent(mainPlayer\currRoom,r)) Then hide = False
		EndIf
		If (hide) Then
			For i=0 To 3
				If (IsRoomAdjacent(mainPlayer\currRoom\adjacent[i],r)) Then
					hide=False : Exit
				EndIf
			Next
		EndIf

		If (hide) Then
			HideEntity(r\obj)
		Else
			ShowEntity(r\obj)
			For i = 0 To MaxRoomLights-1
				If (r\lights[i] <> 0) Then
					dist = EntityDistance(mainPlayer\collider,r\lights[i])
					If (dist < HideDistance) Then
						;TempLightVolume = TempLightVolume + r\lightIntensity[i]*r\lightIntensity[i]*((HideDistance-dist)/HideDistance)
						;ShowEntity(r\lights[i])
					EndIf
				Else
					Exit
				EndIf
			Next
		EndIf
	Next

	;MapFound(Floor(EntityX(mainPlayer\currRoom\obj) / 8.0), Floor(EntityZ(mainPlayer\currRoom\obj) / 8.0)) = 1
	mainPlayer\currRoom\found = True

	;TempLightVolume = Max(TempLightVolume / 4.5, 1.0)

	If (mainPlayer\currRoom<>Null) Then
		SetRoomVisibility(mainPlayer\currRoom,True)
		For i=0 To 3
			If (mainPlayer\currRoom\adjacent[i]<>Null) Then
				x = Abs(EntityX(mainPlayer\collider,True)-EntityX(mainPlayer\currRoom\adjDoor[i]\frameobj,True))
				z = Abs(EntityZ(mainPlayer\collider,True)-EntityZ(mainPlayer\currRoom\adjDoor[i]\frameobj,True))
				If (mainPlayer\currRoom\adjDoor[i]\openstate = 0) Then
					SetRoomVisibility(mainPlayer\currRoom\adjacent[i],False)
				ElseIf ((Not EntityInView(mainPlayer\currRoom\adjDoor[i]\frameobj,mainPlayer\cam))) Then
					SetRoomVisibility(mainPlayer\currRoom\adjacent[i],False)
				Else
					SetRoomVisibility(mainPlayer\currRoom\adjacent[i],True)
				EndIf

				For j=0 To 3
					If (mainPlayer\currRoom\adjacent[i]\adjacent[j]<>Null) Then
						If (mainPlayer\currRoom\adjacent[i]\adjacent[j]<>mainPlayer\currRoom) Then SetRoomVisibility(mainPlayer\currRoom\adjacent[i]\adjacent[j],False)
					EndIf
				Next
			EndIf
		Next
	EndIf

End Function

Function IsRoomAdjacent%(this.Room,that.Room)
	If (this=Null) Then Return False
	If (this=that) Then Return True
	Local i%
	For i=0 To 3
		If (that=this\adjacent[i]) Then Return True
	Next
	Return False
End Function

;-------------------------------------------------------------------------------------------------------

Function AddLight%(room.Room, x#, y#, z#, ltype%, range#, r%, g%, b%)
	Local i%
	;TODO: These names suck.
	Local light%, sprite%
	Local lightSpriteTex% = GrabTexture("GFX/Sprites/light_flare.jpg")

	If (room<>Null) Then
		For i = 0 To MaxRoomLights-1
			If (room\lights[i]=0) Then
				room\lights[i] = CreateLight(ltype)
				;room\lightDist[i] = range
				LightRange(room\lights[i],range)
				LightColor(room\lights[i],r,g,b)
				PositionEntity(room\lights[i],x,y,z,True)
				EntityParent(room\lights[i],room\obj)

				room\lightIntensity[i] = (r+g+b)/255.0/3.0

				room\lightSprites[i]= CreateSprite()
				PositionEntity(room\lightSprites[i], x, y, z)
				ScaleSprite(room\lightSprites[i], 0.13 , 0.13)
				EntityTexture(room\lightSprites[i], lightSpriteTex)
				EntityBlend(room\lightSprites[i], 3)

				EntityParent(room\lightSprites[i], room\obj)

				room\lightSpritesPivot[i] = CreatePivot()
				EntityRadius(room\lightSpritesPivot[i],0.05)
				PositionEntity(room\lightSpritesPivot[i], x, y, z)
				EntityParent(room\lightSpritesPivot[i], room\obj)

				HideEntity(room\lights[i])

				Return room\lights[i]
			EndIf
		Next
	Else
		light=CreateLight(ltype)
		LightRange(light,range)
		LightColor(light,r,g,b)
		PositionEntity(light,x,y,z,True)
		sprite=CreateSprite()
		PositionEntity(sprite, x, y, z)
		ScaleSprite(sprite, 0.13 , 0.13)
		EntityTexture(sprite, lightSpriteTex)
		EntityBlend(sprite, 3)
		Return light
	EndIf
	DropAsset(lightSpriteTex)
End Function

Type LightTemplate
	Field roomtemplate.RoomTemplate
	Field ltype%
	Field x#, y#, z#
	Field range#
	Field r%, g%, b%

	Field pitch#, yaw#
	Field innerconeangle#, outerconeangle#
End Type

Const LIGHTTYPE_POINT% = 2
Const LIGHTTYPE_SPOT% = 3

Function AddTempLight.LightTemplate(rt.RoomTemplate, x#, y#, z#, ltype%, range#, r%, g%, b%)
	Local lt.LightTemplate = New LightTemplate
	lt\roomtemplate = rt
	lt\x = x
	lt\y = y
	lt\z = z
	lt\ltype = ltype
	lt\range = range
	lt\r = r
	lt\g = g
	lt\b = b

	Return lt
End Function

;-------------------------------------------------------------------------------------------------------

Type TempWayPoint
	Field x#, y#, z#
	Field connectedTo%[32]
	Field roomtemplate.RoomTemplate
End Type

Type WayPoint
	Field obj%
	Field room.Room
	Field state%
	;Field tempDist#
	;Field tempSteps%
	Field connected.WayPoint[16]
	Field dist#[16]

	Field fCost#, gCost#, hCost#

	Field parent.WayPoint
End Type

Function CreateWaypoint.WayPoint(x#,y#,z#,room.Room)
	Local w.WayPoint = New WayPoint

	w\obj = CreatePivot()
	PositionEntity(w\obj, x,y,z)

	EntityParent(w\obj, room\obj)

	w\room = room

	Return w
End Function

Function InitWayPoints(loadingstart%=45)

	Local d.Door, w.WayPoint, w2.WayPoint, r.Room, ClosestRoom.Room

	Local x#, y#, z#, i%, tline%

	Local temper% = MilliSecs()

	Local dist#, dist2#

	For w = Each WayPoint
		EntityPickMode(w\obj, 0, 0)
		EntityRadius(w\obj, 0)

		For i = 0 To 4
			If (w\connected[i]<>Null) Then
				tline = CreateLine(EntityX(w\obj,True),EntityY(w\obj,True),EntityZ(w\obj,True),EntityX(w\connected[i]\obj,True),EntityY(w\connected[i]\obj,True),EntityZ(w\connected[i]\obj,True))
				EntityColor(tline, 255,0,0)
				EntityParent(tline, w\obj)
			EndIf
		Next
	Next

	DebugLog("InitWaypoints() - "+Str(TimeInPosMilliSecs()-temper))

End Function

Function RemoveWaypoint(w.WayPoint)
	FreeEntity(w\obj)
	Delete w
End Function

Function FindPath%(n.NPC, x#, y#, z#)

	DebugLog("findpath: "+Str(n\npcType))

	Local temp%, dist#, dist2#
	Local xtemp#, ytemp#, ztemp#
	Local gtemp#

	Local w.WayPoint, StartPoint.WayPoint, EndPoint.WayPoint
	Local currpoint.WayPoint
	Local twentiethpoint.WayPoint

	Local length% = 0

	Local StartX% = Int(Floor(EntityX(n\collider,True) / 8.0 + 0.5)), StartZ% = Int(Floor(EntityZ(n\collider,True) / 8.0 + 0.5))
       ;If (StartX < 0 Or StartX > MapWidth) Then Return 2
       ;If (StartZ < 0 Or StartZ > MapWidth) Then Return 2

	Local EndX% = Int(Floor(x / 8.0 + 0.5)), EndZ% = Int(Floor(z / 8.0 + 0.5))
       ;If (EndX < 0 Or EndX > MapWidth) Then Return 2
       ;If (EndZ < 0 Or EndZ > MapWidth) Then Return 2

	Local CurrX%, CurrZ%

       ;pathstatus = 0, ei ole etsitty reitti�
       ;pathstatus = 1, reitti l�ydetty
       ;pathstatus = 2, reitti� ei ole olemassa

	For w = Each WayPoint
		w\state = 0
		w\fCost = 0
		w\gCost = 0
		w\hCost = 0
	Next

	n\pathStatus = 0
	n\pathLocation = 0

	Local i%
	For i = 0 To 19
		n\path[i] = Null
	Next

	Local pvt% = CreatePivot()
	PositionEntity(pvt, x,y,z, True)

	temp = CreatePivot()
	PositionEntity(temp, EntityX(n\collider,True), EntityY(n\collider,True)+0.15, EntityZ(n\collider,True))

	dist = 350.0
	For w = Each WayPoint
		xtemp = EntityX(w\obj,True)-EntityX(temp,True)
          ;If (xtemp < 8.0) Then
		ztemp = EntityZ(w\obj,True)-EntityZ(temp,True)
             ;If (ztemp < 8.0) Then
		ytemp = EntityY(w\obj,True)-EntityY(temp,True)
                ;If (ytemp < 8.0) Then
		dist2 = (xtemp*xtemp)+(ytemp*ytemp)+(ztemp*ztemp)
		If (dist2 < dist) Then
			;prefer waypoints that are visible
			If (Not EntityVisible(w\obj, temp)) Then dist2 = dist2*3
			If (dist2 < dist) Then
				dist = dist2
				StartPoint = w
			EndIf
		EndIf
                ;EndIf
             ;EndIf
          ;EndIf
	Next
	DebugLog("DIST: "+Str(dist))

	FreeEntity(temp)

	If (StartPoint = Null) Then Return 2
	StartPoint\state = 1

       ;If (EndPoint = Null) Then
	EndPoint = Null
	dist = 400.0
	For w = Each WayPoint
		xtemp = EntityX(pvt,True)-EntityX(w\obj,True)
          ;If (xtemp =< 8.0) Then
		ztemp = EntityZ(pvt,True)-EntityZ(w\obj,True)
             ;If (ztemp =< 8) Then
		ytemp = EntityY(pvt,True)-EntityY(w\obj,True)
		dist2 = (xtemp*xtemp)+(ytemp*ytemp)+(ztemp*ztemp)

		If (dist2 < dist) Then ; And EntityVisible(w\obj, pvt)
			dist = dist2
			EndPoint = w
		EndIf
             ;EndIf
          ;EndIf
	Next
       ;EndIf

	FreeEntity(pvt)

	If (EndPoint = StartPoint) Then
		If (dist < 0.4) Then
			Return 0
		Else
			n\path[0]=EndPoint
			Return 1
		EndIf
	EndIf
	If (EndPoint = Null) Then Return 2

       ;aloitus- ja lopetuspisteet l�ydetty, aletaan etsi� reitti�

	Local smallest.WayPoint
	Repeat

		temp = False
		smallest = Null
		dist = 10000.0
		For w = Each WayPoint
			If (w\state = 1) Then
                temp = True
                If ((w\fCost) < dist) Then
					dist = w\fCost
					smallest = w
                EndIf
			EndIf
		Next

		If (smallest <> Null) Then

			w = smallest
			w\state = 2

			For i = 0 To 15
                If (w\connected[i]<>Null) Then
					If (w\connected[i]\state < 2) Then

						If (w\connected[i]\state=1) Then ;open list
							gtemp = w\gCost+w\dist[i]
							;TODO: fix?
							;If (n\npcType = NPCtypeMTF) Then
							;	If (w\connected[i]\door = Null) Then gtemp = gtemp + 0.5
							;EndIf
							If (gtemp < w\connected[i]\gCost) Then ;parempi reitti -> overwrite
								w\connected[i]\gCost = gtemp
								w\connected[i]\fCost = w\connected[i]\gCost + w\connected[i]\hCost
								w\connected[i]\parent = w
							EndIf
						Else
							w\connected[i]\hCost = Abs(EntityX(w\connected[i]\obj,True)-EntityX(EndPoint\obj,True))+Abs(EntityZ(w\connected[i]\obj,True)-EntityZ(EndPoint\obj,True))
							gtemp = w\gCost+w\dist[i]
							;TODO: fix?
							;If (n\npcType = NPCtypeMTF) Then
							;	If (w\connected[i]\door = Null) Then gtemp = gtemp + 0.5
							;EndIf
							w\connected[i]\gCost = gtemp
							w\connected[i]\fCost = w\gCost+w\hCost
							w\connected[i]\parent = w
							w\connected[i]\state=1
						EndIf
					EndIf

                EndIf
			Next
		Else ;open listilt� ei l�ytynyt mit��n
			If (EndPoint\state > 0) Then
                StartPoint\parent = Null
                EndPoint\state = 2
                Exit
			EndIf
		EndIf

		If (EndPoint\state > 0) Then
			StartPoint\parent = Null
			EndPoint\state = 2
			Exit
		EndIf

	Until temp = False

	If (EndPoint\state > 0) Then

		currpoint = EndPoint
		twentiethpoint = EndPoint

		length = 0
		Repeat
			length = length +1
			currpoint = currpoint\parent
			If (length>20) Then
                twentiethpoint = twentiethpoint\parent
			EndIf
		Until currpoint = Null

		currpoint = EndPoint
		While (twentiethpoint<>Null)
			length=Int(Min(length-1,19))
             ;DebugLog("LENGTH "+length)
			twentiethpoint = twentiethpoint\parent
			n\path[length] = twentiethpoint
		Wend

		Return 1
          ;RuntimeError(length)
    ;      For i = 0 To (length-1)
    ;         temp =False
    ;         If (length < 20) Then
    ;            n\path[length-1-i] = currpoint.WayPoint
    ;         Else
    ;            If (i < 20) Then
    ;               n\path[20-1-i] = w.WayPoint
    ;            Else
    ;               ;Return 1
    ;            EndIf
    ;         EndIf
    ;
    ;         If (currpoint = StartPoint) Then Return 1
    ;
    ;         If (currpoint\parent <> Null) Then
    ;            currpoint = currpoint\parent
    ;         Else
    ;            Exit
    ;         EndIf
    ;
    ;      Next

	Else

		DebugLog("FUNCTION FindPath() - reitti� ei l�ytynyt")
		Return 2 ;reitti� m��r�np��h�n ei l�ytynyt

	EndIf

End Function

Function CreateLine%(x1#,y1#,z1#, x2#,y2#,z2#, mesh%=0)
	Local surf%, verts%

	If (mesh = 0) Then
		mesh=CreateMesh()
		EntityFX(mesh,16)
		surf=CreateSurface(mesh)
		verts = 0

		AddVertex(surf,x1,y1,z1,0,0)
	Else
		surf = GetSurface(mesh,1)
		verts = CountVertices(surf)-1
	EndIf

	AddVertex(surf,(x1+x2)/2,(y1+y2)/2,(z1+z2)/2,0,0)
	; you could skip creating the above vertex and change the line below to
	; AddTriangle(surf,verts,verts+1,verts+0)
	; so your line mesh would use less vertices, the drawback is that some videocards (like the matrox g400)
	; aren't able to create a triangle with 2 vertices. so, it's your call :)
	AddVertex(surf,x2,y2,z2,1,0)

	AddTriangle(surf,verts,verts+2,verts+1)

	Return mesh
End Function

;-------------------------------------------------------------------------------------------------------

Global SelectedScreen.Screen
Type Screen
	Field obj%
	Field imgpath$
	Field img%
	Field room.Room
End Type

Type TempScreen
	Field imgpath$
	Field x#,y#,z#
	Field roomtemplate.RoomTemplate
End Type

Function CreateScreen.Screen(x#,y#,z#,imgpath$,r.Room)
	Local s.Screen = New Screen
	s\obj = CreatePivot()
	EntityPickMode(s\obj, 1)
	EntityRadius(s\obj, 0.1)

	PositionEntity(s\obj, x,y,z)
	s\imgpath = imgpath
	s\room = r
	EntityParent(s\obj, r\obj)

	Return s
End Function

Function UpdateScreens()
	If (SelectedScreen <> Null) Then Return
	If (mainPlayer\selectedDoor <> Null) Then Return

	Local s.Screen
	For s = Each Screen
		If (s\room = mainPlayer\currRoom) Then
			If (EntityDistance(mainPlayer\collider,s\obj)<1.2) Then
				EntityPick(mainPlayer\cam, 1.2)
				If (PickedEntity()=s\obj And s\imgpath<>"") Then
					DrawHandIcon=True
					If (MouseUp1) Then
						SelectedScreen=s
						s\img = LoadImage("GFX/screens/"+s\imgpath)
						s\img = ResizeImage2(s\img, Int(ImageWidth(s\img) * MenuScale), Int(ImageHeight(s\img) * MenuScale))
						MaskImage(s\img, 255,0,255)
						PlaySound_SM(sndManager\button)
						MouseUp1=False
					EndIf
				EndIf
			EndIf
		EndIf
	Next

End Function

;-------------------------------------------------------------------------------------------------------

Global SelectedMonitor.SecurityCam
Global CoffinCam.SecurityCam
Type SecurityCam
	Field obj%, monitorObj%

	Field baseObj%, cameraObj%

	Field scrObj%, scrWidth#, scrHeight#
	Field screen%, cam%, scrTexture%, scrOverlay%
	Field angle#, turn#, currAngle#
	Field state#, playerState%

	Field soundCHN%

	Field inSight%

	Field renderInterval#

	Field room.Room

	Field followPlayer%
	Field coffinEffect%

	Field allowSaving%

	Field minAngle#, maxAngle#, dir%

	Field isRoom2slCam% = False
	Field room2slTexs%[2]
	Field specialCam% = False
	Field id% = -1
End Type

Global ScreenTexs%[2]

Function CreateSecurityCam.SecurityCam(x#, y#, z#, r.Room, screen% = False)
	Local sc.SecurityCam = New SecurityCam
	Local scale#

	sc\obj = CopyEntity(CamBaseOBJ)
	ScaleEntity(sc\obj, 0.0015, 0.0015, 0.0015)
	sc\cameraObj = CopyEntity(CamOBJ)
	ScaleEntity(sc\cameraObj, 0.01, 0.01, 0.01)

	sc\room = r

	sc\screen = screen
	If (screen) Then
		sc\allowSaving = True

		sc\renderInterval = 12

		scale = RoomScale * 4.5 * 0.4

		sc\scrObj = CreateSprite()
		EntityFX(sc\scrObj, 17)
		SpriteViewMode(sc\scrObj, 2)
		sc\scrTexture = 0
		EntityTexture(sc\scrObj, ScreenTexs[sc\scrTexture])
		ScaleSprite(sc\scrObj, MeshWidth(Monitor) * scale * 0.95 * 0.5, MeshHeight(Monitor) * scale * 0.95 * 0.5)

		sc\scrOverlay = CreateSprite(sc\scrObj)
		;	scaleSprite(sc\scrOverlay , 0.5, 0.4)
		ScaleSprite(sc\scrOverlay, MeshWidth(Monitor) * scale * 0.95 * 0.5, MeshHeight(Monitor) * scale * 0.95 * 0.5)
		MoveEntity(sc\scrOverlay, 0, 0, -0.0005)
		EntityTexture(sc\scrOverlay, MonitorTexture)
		SpriteViewMode(sc\scrOverlay, 2)
		EntityBlend(sc\scrOverlay , 3)

		sc\monitorObj = CopyEntity(Monitor, sc\scrObj)

		ScaleEntity(sc\monitorObj, scale, scale, scale)

		sc\cam = CreateCamera()
		CameraViewport(sc\cam, 0, 0, 512, 512)
		CameraRange(sc\cam, 0.05, 6.0)
		CameraZoom(sc\cam, 0.8)
		HideEntity(sc\cam)
	EndIf

	PositionEntity(sc\obj, x, y, z)

	If (r<>Null) Then EntityParent(sc\obj, r\obj)

	Return sc
End Function

Function UpdateSecurityCams()
	Local sc.SecurityCam
	Local close%, temp#, pvt%
	Local i%
	Local gorePics%[GORE_PIC_COUNT]
	Local aiPic%

	PlayerDetected = False

	;coffineffect = 0, not affected by 895
	;coffineffect = 1, constantly affected by 895
	;coffineffect = 2, 079 can broadcast 895 feed on this screen
	;coffineffect = 3, 079 broadcasting 895 feed

	For sc = Each SecurityCam
		close = False
		If (sc\room = Null And (Not sc\specialCam)) Then
			HideEntity(sc\cam)
		Else
			If (Not sc\specialCam) Then
				If (sc\room\dist < 6.0 Or mainPlayer\currRoom=sc\room) Then
					close = True
				ElseIf ((sc\isRoom2slCam)) Then
					close = True
				ElseIf ((sc\cam<>0)) Then
					HideEntity(sc\cam)
				EndIf
			EndIf

			If (sc\isRoom2slCam) Then sc\coffinEffect = 0
			If (sc\room <> Null) Then
				If (sc\room\roomTemplate\name = "hll_sl_2") Then sc\coffinEffect = 0
			EndIf
			If (sc\specialCam) Then sc\coffinEffect = 0

			If (close Or sc=CoffinCam Or sc\isRoom2slCam) Then
				If (sc\followPlayer) Then
					If (sc<>CoffinCam) Then
						If (EntityVisible(sc\cameraObj,mainPlayer\cam)) Then
							PlayerDetected = True
						EndIf
					EndIf
					PointEntity(sc\cameraObj, mainPlayer\cam)
					temp = EntityPitch(sc\cameraObj)
					RotateEntity(sc\obj, 0, CurveAngle(EntityYaw(sc\cameraObj), EntityYaw(sc\obj), 75.0), 0)

					If (temp < 40.0) Then temp = 40
					If (temp > 70.0) Then temp = 70
					RotateEntity(sc\cameraObj, CurveAngle(temp, EntityPitch(sc\cameraObj), 75.0), EntityYaw(sc\obj), 0)

					PositionEntity(sc\cameraObj, EntityX(sc\obj, True), EntityY(sc\obj, True) - 0.083, EntityZ(sc\obj, True))
					RotateEntity(sc\cameraObj, EntityPitch(sc\cameraObj), EntityYaw(sc\obj), 0)
				Else
					If (sc\turn > 0) Then
						If (sc\dir = 0) Then
							sc\currAngle=sc\currAngle+0.2 * timing\tickDuration
							If (sc\currAngle > (sc\turn * 1.3)) Then sc\dir = 1
						Else
							sc\currAngle=sc\currAngle-0.2 * timing\tickDuration
							If (sc\currAngle < (-sc\turn * 1.3)) Then sc\dir = 0
						EndIf
					EndIf
					RotateEntity(sc\obj, 0, sc\room\angle + sc\angle + Max(Min(sc\currAngle, sc\turn), -sc\turn), 0)

					PositionEntity(sc\cameraObj, EntityX(sc\obj, True), EntityY(sc\obj, True) - 0.083, EntityZ(sc\obj, True))
					RotateEntity(sc\cameraObj, EntityPitch(sc\cameraObj), EntityYaw(sc\obj), 0)

					If (sc\cam<>0) Then
						PositionEntity(sc\cam, EntityX(sc\cameraObj, True), EntityY(sc\cameraObj, True), EntityZ(sc\cameraObj, True))
						RotateEntity(sc\cam, EntityPitch(sc\cameraObj), EntityYaw(sc\cameraObj), 0)
						MoveEntity(sc\cam, 0, 0, 0.1)
					EndIf

					If (sc<>CoffinCam) Then
						If (Abs(DeltaYaw(sc\cameraObj,mainPlayer\cam))<60.0) Then
							If (EntityVisible(sc\cameraObj,mainPlayer\cam)) Then
								PlayerDetected = True
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf

			If (close = True Or sc\isRoom2slCam Or sc\specialCam) Then
				If (sc\screen) Then
					sc\state = sc\state+timing\tickDuration

					If (sc\inSight And sc\allowSaving) Then
						If (SelectedDifficulty\saveType = SAVEONSCREENS And EntityDistance(mainPlayer\cam, sc\scrObj)<1.0) Then
							DrawHandIcon = True
							If (MouseHit1) Then SelectedMonitor = sc
						ElseIf ((SelectedMonitor = sc)) Then
							SelectedMonitor = Null
						EndIf
					Else
						SelectedMonitor = Null
					EndIf

					If (sc\state >= sc\renderInterval) Then
						sc\inSight = False
						If (mainPlayer\blinkTimer > - 5 And EntityInView(sc\scrObj, mainPlayer\cam)) Then
							If (EntityVisible(mainPlayer\cam,sc\scrObj)) Then
								sc\inSight = True

								If (sc\coffinEffect=1 Or sc\coffinEffect=3) Then
									If (mainPlayer\blinkTimer > - 5) Then mainPlayer\sanity895=mainPlayer\sanity895-(timing\tickDuration * 16)

									If (mainPlayer\sanity895 < (-1000)) Then
										DeathMSG = Chr(34)+"What we know is that he died of cardiac arrest. My guess is that it was caused by SCP-895, although it has never been observed affecting video equipment from this far before. "
										DeathMSG = DeathMSG + "Further testing is needed to determine whether SCP-895's "+Chr(34)+"Red Zone"+Chr(34)+" is increasing."+Chr(34)

										Kill(mainPlayer)
									EndIf
								EndIf

								If (Not sc\isRoom2slCam) Then
									If (Not sc\specialCam) Then
										If (CoffinCam = Null Or Rand(5)=5 Or sc\coffinEffect <> 3) Then
											HideEntity(mainPlayer\cam)
											ShowEntity(sc\cam)
											Cls()

											SetBuffer(BackBuffer())
											RenderWorld()
											CopyRect(0,0,512,512,0,0,BackBuffer(),TextureBuffer(ScreenTexs[sc\scrTexture]))

											HideEntity(sc\cam)
											ShowEntity(mainPlayer\cam)
										Else
											HideEntity(mainPlayer\cam)
											ShowEntity(CoffinCam\room\obj)
											SetRoomVisibility(CoffinCam\room,True)
											ShowEntity(CoffinCam\cam)
											Cls()

											SetBuffer(BackBuffer())
											RenderWorld()
											CopyRect(0,0,512,512,0,0,BackBuffer(),TextureBuffer(ScreenTexs[sc\scrTexture]))

											HideEntity(CoffinCam\room\obj)
											HideEntity(CoffinCam\cam)
											ShowEntity(mainPlayer\cam)
										EndIf
									Else
										HideEntity(mainPlayer\cam)
										ShowEntity(sc\cam)
										Cls()

										RenderWorld()

										HideEntity(sc\cam)
										ShowEntity(mainPlayer\cam)

										CopyRect(0,0,512,512,0,0,BackBuffer(),TextureBuffer(sc\room2slTexs[sc\scrTexture]))

									EndIf
								Else
									HideEntity(mainPlayer\cam)
									ShowEntity(sc\room\obj)
									SetRoomVisibility(sc\room,True)
									ShowEntity(sc\cam)
									Cls()

									RenderWorld()

									HideEntity(sc\room\obj)
									HideEntity(sc\cam)
									ShowEntity(mainPlayer\cam)

									CopyRect(0, 0, userOptions\screenWidth, userOptions\screenHeight, 0, 0, BackBuffer(), TextureBuffer(sc\room2slTexs[sc\scrTexture]))
								EndIf

							EndIf
						EndIf
						sc\state = 0
					EndIf

					If (SelectedMonitor = sc Or sc\coffinEffect=1 Or sc\coffinEffect=3) Then
						If (sc\inSight) Then
						;If ((Not NoClip)) Then
							pvt = CreatePivot()
							PositionEntity(pvt, EntityX(mainPlayer\cam), EntityY(mainPlayer\cam), EntityZ(mainPlayer\cam))
							PointEntity(pvt, sc\scrObj)

							DebugLog("curvea: "+Str(CurveAngle(EntityYaw(pvt), EntityYaw(mainPlayer\collider), Min(Max(15000.0 / (-mainPlayer\sanity895), 20.0), 200.0))))
							RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), CurveAngle(EntityYaw(pvt), EntityYaw(mainPlayer\collider), Min(Max(15000.0 / (-mainPlayer\sanity895), 20.0), 200.0)), 0)

							TurnEntity(pvt, 90, 0, 0)
							mainPlayer\headPitch = CurveAngle(EntityPitch(pvt), mainPlayer\headPitch + 90.0, Min(Max(15000.0 / (-mainPlayer\sanity895), 20.0), 200.0))
							mainPlayer\headPitch=mainPlayer\headPitch-90

							DebugLog("pvt: "+Str(EntityYaw(pvt))+"   - coll: "+Str(EntityYaw(mainPlayer\collider)))


							FreeEntity(pvt)
						;EndIf
							If (sc\coffinEffect=1 Or sc\coffinEffect=3) Then
								For i=0 To GORE_PIC_COUNT-1
									gorePics[i] = GrabTexture("GFX/895pics/pic" + Str(i + 1) + ".jpg")
								Next
								If (mainPlayer\sanity895 < - 800) Then
									If (Rand(3) = 1) Then EntityTexture(sc\scrOverlay, MonitorTexture)
									If (Rand(6) < 5) Then
										EntityTexture(sc\scrOverlay, gorePics[Rand(0, GORE_PIC_COUNT-1)])
										;If (sc\playerState = 1) Then PlaySound(HorrorSFX(1)) ;TODO: fix
										sc\playerState = 2
										If (sc\soundCHN = 0) Then
											;sc\soundCHN = PlaySound(HorrorSFX(4)) ;TODO: fix
										Else
											;If (Not IsChannelPlaying(sc\soundCHN)) Then sc\soundCHN = PlaySound(HorrorSFX(4)) ;TODO: fix
										EndIf
										If (sc\coffinEffect=3 And Rand(200)=1) Then
											sc\coffinEffect=2 : sc\playerState = Rand(10000, 20000)
										EndIf
									EndIf
									mainPlayer\blurTimer = 1000
								ElseIf ((mainPlayer\sanity895 < - 500)) Then
									If (Rand(7) = 1) Then EntityTexture(sc\scrOverlay, MonitorTexture)
									If (Rand(50) = 1) Then
										EntityTexture(sc\scrOverlay, gorePics[Rand(0, GORE_PIC_COUNT-1)])
										;If (sc\playerState = 0) Then PlaySound(HorrorSFX(0)) ;TODO: fix
										sc\playerState = Int(Max(sc\playerState, 1))
										If (sc\coffinEffect=3 And Rand(100)=1) Then
											sc\coffinEffect=2 : sc\playerState = Rand(10000, 20000)
										EndIf
									EndIf
								Else
									EntityTexture(sc\scrOverlay, MonitorTexture)
								EndIf
								For i=0 To GORE_PIC_COUNT-1
									DropAsset(gorePics[i])
								Next
							EndIf
						EndIf
					EndIf

					If (sc\inSight And sc\coffinEffect=0 Or sc\coffinEffect=2) Then
						aiPic = GrabTexture("GFX/079pics/face.jpg")
						If (sc\playerState = 0) Then
							sc\playerState = Rand(60000, 65000)
						EndIf

						If (Rand(500) = 1) Then
							EntityTexture(sc\scrOverlay, aiPic)
						EndIf

						If (TimeInPosMilliSecs() Mod sc\playerState) >= Rand(600) Then
							EntityTexture(sc\scrOverlay, MonitorTexture)
						Else
							If (sc\soundCHN = 0) Then
								sc\soundCHN = PlaySound(LoadTempSound("SFX/SCP/079/Broadcast"+Str(Rand(1,3))+".ogg"))
								If (sc\coffinEffect=2) Then
									sc\coffinEffect=3 : sc\playerState = 0
								EndIf
							ElseIf ((Not IsChannelPlaying(sc\soundCHN))) Then
								sc\soundCHN = PlaySound(LoadTempSound("SFX/SCP/079/Broadcast"+Str(Rand(1,3))+".ogg"))
								If (sc\coffinEffect=2) Then
									sc\coffinEffect=3 : sc\playerState = 0
								EndIf
							EndIf
							EntityTexture(sc\scrOverlay, aiPic)
						EndIf
						DropAsset(aiPic)
					EndIf

				EndIf ;if screen=true

				If (Not sc\inSight) Then sc\soundCHN = LoopRangedSound_SM(sndManager\camera, sc\soundCHN, mainPlayer\cam, sc\cameraObj, 4.0)
			Else
				If (SelectedMonitor=sc) Then SelectedMonitor=Null
			EndIf
		EndIf
	Next

	Cls()


End Function

;-------------------------------------------------------------------------------------------------------

Type Prop
	Field file$
	Field obj%

	Field x#,y#,z#
	Field pitch#,yaw#,roll#
	Field xScale#,yScale#,zScale#
End Type

Function LoadProp.Prop(file$,x#,y#,z#,pitch#,yaw#,roll#,xScale#,yScale#,zScale#)
	Local p.Prop
	p = New Prop
	p\file = file
	p\x = x
	p\y = y
	p\z = z
	p\pitch = pitch
	p\yaw = yaw
	p\roll = roll
	p\xScale = xScale
	p\yScale = yScale
	p\zScale = zScale

	Local p2.Prop
	For p2 = Each Prop
		If ((p<>p2) And (p2\file = file)) Then
			p\obj = CopyEntity(p2\obj)
			Exit
		EndIf
	Next

	If (p\obj=0) Then p\obj = LoadMesh("GFX/Map/Props/"+file+".b3d")
	If (p\obj=0) Then RuntimeError(file)
	HideEntity(p\obj)
	Return p
End Function

;-------------------------------------------------------------------------------------------------------

Global MapRooms.IntArray = Null ;TODO: replace with an array of the proper type after moving to C++

Function CreateMap()
	Local i%, c%, j%

	DebugLog("Generating a map using the seed "+RandomSeed)

	SeedRnd(SeedStringToInt(RandomSeed))

	Local mapDim% = MAP_SIZE
	Local layout.IntArray = CreateIntArray(mapDim,mapDim)
	MapRooms = CreateIntArray(mapDim,mapDim)

	;clear the grid
	Local y%, x%
	For y = 0 To mapDim-1
		For x = 0 To mapDim-1
			SetIntArrayElem(layout,0,x,y)
			SetIntArrayElem(MapRooms,0,x,y)
		Next
	Next

	;4x4 squares, offset 1 slot from 0,0
	Local rectWidth% = 3
	Local rectHeight% = 3

	For y = 0 To mapDim-1
		For x = 0 To mapDim-1
			If ((x Mod rectWidth=1) Or (y Mod rectHeight=1)) Then
				If ((x>=rectWidth And x<mapDim-rectWidth) Or (y>=rectHeight And y<mapDim-rectHeight)) Then
					SetIntArrayElem(layout,1,x,y)
				EndIf
			EndIf
		Next
	Next

	DetermineRoomTypes(layout,mapDim)

	;shift some horizontal corridors
	Local shift%
	Local nonShiftStreak% = Rand(0,5)
	For y = 1 To mapDim-2
		For x = 0 To mapDim-2
			If (y>6 Or x>6) Then
				If ((y Mod rectHeight=1) And GetIntArrayElem(layout,x,y)=ROOM2) Then
					shift = Rand(0,1)
					If (nonShiftStreak=0) Then shift = 0
					If (nonShiftStreak>5) Then shift = 1
					If ((x/rectWidth) Mod 2) Then shift = -shift
					If (shift<>0) Then
						For i = 0 To rectWidth-2
							SetIntArrayElem(layout,0,x+i,y)
							SetIntArrayElem(layout,ROOM2,x+i,y+shift)
						Next
						nonShiftStreak = 0
					Else
						nonShiftStreak=nonShiftStreak+1
					EndIf
					x = x+rectWidth-1
				EndIf
			EndIf
		Next
	Next

	DetermineRoomTypes(layout,mapDim)

	;punch out some holes to get room2c's
	Local punchOffset% = Rand(0,1)
	Local roomAbove%
	Local roomBelow%
	For y = 2 To mapDim-4
		For x = 0 To mapDim-1
			If ((((x/rectWidth) Mod 2)=punchOffset) And (GetIntArrayElem(layout,x,y)=ROOM2)) Then
				roomAbove = GetIntArrayElem(layout,x,y-1)
				roomBelow = GetIntArrayElem(layout,x,y+1)
				If ((roomAbove>=ROOM2) And (roomBelow>=ROOM2)) And ((roomAbove+roomBelow)>(ROOM2+ROOM3)) Then
					SetIntArrayElem(layout,0,x,y)
				EndIf
			EndIf
		Next
	Next

	DetermineRoomTypes(layout,mapDim)

	Local zone% = ZONE_LCZ

	;start off by placing rooms that ask to be placed a certain amount of times
	Local prioritizedTemplateCount% = 0
	Local rt.RoomTemplate

	For rt = Each RoomTemplate
		If (((rt\zones And zone)<>0) And (rt\maxAmount>0) And (rt\shape<>ROOM0)) Then
			prioritizedTemplateCount=prioritizedTemplateCount+1
		EndIf
	Next
	Local prioritizedTemplates.IntArray = CreateIntArray(prioritizedTemplateCount,1) ;TODO: replace with an array of the right type once we move to C++
	Local tempTemplate.RoomTemplate
	Local tempTemplate2.RoomTemplate
	SetIntArrayElem(prioritizedTemplates,0,0,0)
	For rt = Each RoomTemplate
		If (((rt\zones And zone)<>0) And (rt\maxAmount>0) And (rt\shape<>ROOM0)) Then
			tempTemplate = rt
			DebugLog("queueing up "+rt\name)
			For i=0 To prioritizedTemplateCount-1
				If (GetIntArrayElem(prioritizedTemplates,i,0)=0) Then
					If (i<prioritizedTemplateCount-1) Then
						SetIntArrayElem(prioritizedTemplates,0,i+1,0)
					EndIf
					SetIntArrayElem(prioritizedTemplates,Handle(tempTemplate),i,0)
					Exit
				Else
					tempTemplate2 = Object.RoomTemplate(GetIntArrayElem(prioritizedTemplates,i,0))
					If (tempTemplate2\maxAmount>tempTemplate\maxAmount) Then
						SetIntArrayElem(prioritizedTemplates,Handle(tempTemplate),i,0)
						;DebugLog("swapping "+tempTemplate2\name+" for "+tempTemplate\name)
						tempTemplate = tempTemplate2
					EndIf
				EndIf
			Next
		EndIf
	Next

	Local RoomCount%[ROOM4+1]
	For y = 0 To mapDim-1
		For x = 0 To mapDim-1
			If (GetIntArrayElem(layout,x,y)<>ROOM0) Then RoomCount[GetIntArrayElem(layout,x,y)]=RoomCount[GetIntArrayElem(layout,x,y)]+1
		Next
	Next
	For i = 1 To ROOM4
		DebugLog("Type"+Str(i)+" count: "+Str(RoomCount[i]))
	Next

	Local r.Room

	Local placementCount%
	Local loopStartX%
	Local loopStartY%
	Local loopEndX%
	Local loopEndY%
	Local loopX%
	Local loopY%
	Local offsetX%
	Local offsetY%
	Local placed%

	Local k%
	For k=0 To prioritizedTemplateCount-1
		rt = Object.RoomTemplate(GetIntArrayElem(prioritizedTemplates,k,0))

		placementCount = Rand(rt\minAmount,rt\maxAmount)

		DebugLog("trying to place "+Str(placementCount)+" "+rt\name)

		For c = 1 To placementCount
			loopStartX = Int(Min(Floor(Float(mapDim)*rt\xRangeStart),mapDim-1))
			loopStartY = Int(Min(Floor(Float(mapDim)*rt\yRangeStart),mapDim-1))
			loopEndX = Int(Min(Floor(Float(mapDim)*rt\xRangeEnd),mapDim-1))
			loopEndY = Int(Min(Floor(Float(mapDim)*rt\yrangeEnd),mapDim-1))

			loopX = loopEndX-loopStartX
			loopY = loopEndY-loopStartY

			offsetX = Rand(0,loopX)
			offsetY = Rand(0,loopY)

			placed = False

			For j = 0 To loopY
				For i = 0 To loopX
					x = ((i+offsetX) Mod (loopX+1)) + loopStartX
					y = ((j+offsetY) Mod (loopY+1)) + loopStartY
					If (GetIntArrayElem(layout,x,y)>0) And (GetIntArrayElem(layout,x,y)=rt\shape) Then
						r = CreateRoom(rt,x*8.0,0.0,y*8.0)
						r\angle = DetermineRotation(layout,x,y)
						TurnEntity(r\obj,0,r\angle,0)
						SetIntArrayElem(layout,-1,x,y) ;mark as used
						SetIntArrayElem(MapRooms,Handle(r),x,y) ;add to the MapRooms array
						placed = True
					EndIf

					If (placed) Then Exit
				Next
				If (placed) Then Exit
			Next
			If (Not placed) Then RuntimeError("(seed: "+RandomSeed+") Failed To place "+rt\name+" around ("+Str(loopStartX)+","+Str(loopStartY)+","+Str(loopEndX)+","+Str(loopEndY)+")")
		Next
	Next

	DeleteIntArray(prioritizedTemplates)

	Local randomTemplateCount%
	Local totalCommonness%[ROOM4+1]
	For i = 1 To ROOM4
		totalCommonness[i] = 0
	Next
	For rt = Each RoomTemplate
		If (((rt\zones And zone)<>0) And (rt\maxAmount<0) And (rt\shape<>ROOM0)) Then
			randomTemplateCount=randomTemplateCount+1
			totalCommonness[rt\shape]=totalCommonness[rt\shape]+Int(rt\commonness)
		EndIf
	Next
	Local randomTemplates.IntArray = CreateIntArray(randomTemplateCount,1)
	Local index% = 0
	Local tempHandle1%
	Local tempHandle2%

	For rt = Each RoomTemplate
		If (((rt\zones And zone)<>0) And (rt\maxAmount<0) And (rt\shape<>ROOM0)) Then
			SetIntArrayElem(randomTemplates,Handle(rt),index,0)
			index=index+1
		EndIf
	Next

	;shuffle the templates
	For i = 0 To randomTemplateCount-1
		index = Rand(0,randomTemplateCount-1)
		tempHandle1 = GetIntArrayElem(randomTemplates,i,0)
		tempHandle2 = GetIntArrayElem(randomTemplates,index,0)
		SetIntArrayElem(randomTemplates,tempHandle2,i,0)
		SetIntArrayElem(randomTemplates,tempHandle1,index,0)
	Next

	Local targetCommonness% = 0
	Local commonnessAccumulator% = 0
	Local currType%
	For y = 0 To mapDim-1
		For x = 0 To mapDim-1
			commonnessAccumulator = 0
			currType = GetIntArrayElem(layout,x,y)
			If (currType>0) Then
				targetCommonness = Rand(0,totalCommonness[currType])

				For i = 0 To randomTemplateCount-1
					tempTemplate = Object.RoomTemplate(GetIntArrayElem(randomTemplates,i,0))
					If (tempTemplate\shape = currType) Then
						commonnessAccumulator=commonnessAccumulator+Int(tempTemplate\commonness)
						If (commonnessAccumulator>=targetCommonness) Then
							r = CreateRoom(tempTemplate,x*8.0,0.0,y*8.0)
							r\angle = DetermineRotation(layout,x,y)
							TurnEntity(r\obj,0,r\angle,0)
							SetIntArrayElem(layout,-1,x,y) ;mark as used
							SetIntArrayElem(MapRooms,Handle(r),x,y) ;add to the MapRooms array
							Exit
						EndIf
					EndIf
				Next
			EndIf
		Next
	Next

	DeleteIntArray(randomTemplates)
	DeleteIntArray(layout)

	;finally, let rooms know who their neighbors are
	Local tempX%
	Local tempY%
	Local tempWaypoint.WayPoint
	Local newWaypoint.WayPoint
	Local roomAWaypoint.WayPoint
	Local roomBWaypoint.WayPoint
	For y = 0 To mapDim-1
		For x = 0 To mapDim-1
			r = Object.Room(GetIntArrayElem(MapRooms,x,y))
			If (r<>Null) Then
				For i = 0 To 3
					Select (i)
						Case 0
							tempX = 1
							tempY = 0
						Case 1
							tempX = 0
							tempY = -1
						Case 2
							tempX = -1
							tempY = 0
						Case 3
							tempX = 0
							tempY = 1
					End Select

					If (x+tempX>=0) And (x+tempX<mapDim) And (y+tempY>=0) And (y+tempY<mapDim) Then
						r\adjacent[i] = Object.Room(GetIntArrayElem(MapRooms,x+tempX,y+tempY))
						If (r\adjacent[i]<>Null) Then
							If (r\adjacent[i]\adjDoor[(i+2) Mod 4]=Null) Then
								r\adjDoor[i] = CreateDoor(r\x+4.0*tempX, 0.0,r\z+4.0*tempY, 90.0*((i+1) Mod 2), Null)
								newWaypoint = CreateWaypoint(r\x+4.0*tempX,50.0*RoomScale,r\z+4.0*tempY,r)

								DebugLog("step1")
								roomAWaypoint = Null : roomBWaypoint = Null
								For tempWaypoint = Each WayPoint
									If (tempWaypoint<>newWaypoint) Then
										If (tempWaypoint\room = r) Then
											If (roomAWaypoint = Null) Then
												roomAWaypoint = tempWaypoint
											ElseIf ((EntityDistance(roomAWaypoint\obj,newWaypoint\obj)>EntityDistance(tempWaypoint\obj,newWaypoint\obj))) Then
												roomAWaypoint = tempWaypoint
											EndIf
										EndIf

										If (tempWaypoint\room = r\adjacent[i]) Then
											If (roomBWaypoint = Null) Then
												roomBWaypoint = tempWaypoint
											ElseIf ((EntityDistance(roomBWaypoint\obj,newWaypoint\obj)>EntityDistance(tempWaypoint\obj,newWaypoint\obj))) Then
												roomBWaypoint = tempWaypoint
											EndIf
										EndIf
									EndIf
								Next

								DebugLog("step2")
								If (roomAWaypoint<>Null And roomBWaypoint<>Null) Then
									For j = 0 To 15
										If (roomAWaypoint\connected[j]=Null) Then
											roomAWaypoint\connected[j]=newWaypoint
											Exit
										EndIf
									Next

									For j = 0 To 15
										If (roomBWaypoint\connected[j]=Null) Then
											roomBWaypoint\connected[j]=newWaypoint
											Exit
										EndIf
									Next

									For j = 0 To 15
										If (newWaypoint\connected[j]=Null) Then
											newWaypoint\connected[j]=roomAWaypoint
											newWaypoint\connected[j+1]=roomBWaypoint
											Exit
										EndIf
									Next
								EndIf

								DebugLog("step3")
							Else
								r\adjDoor[i] = r\adjacent[i]\adjDoor[(i+2) Mod 4]
							EndIf
						EndIf
					EndIf
				Next
			EndIf
		Next
	Next
End Function

Function DetermineRoomTypes(layout.IntArray,mapDim%)
	Local horNeighborCount% = 0
	Local vertNeighborCount% = 0

	Local y%, x%
	For y = 0 To mapDim-1
		For x = 0 To mapDim-1
			If (GetIntArrayElem(layout,x,y)<>0) Then
				horNeighborCount = 0
				If (x>0) Then
					horNeighborCount=horNeighborCount+(GetIntArrayElem(layout,x-1,y)<>0)
				EndIf
				If (x<mapDim-1) Then
					horNeighborCount=horNeighborCount+(GetIntArrayElem(layout,x+1,y)<>0)
				EndIf
				vertNeighborCount = 0
				If (y>0) Then
					vertNeighborCount=vertNeighborCount+(GetIntArrayElem(layout,x,y-1)<>0)
				EndIf
				If (y<mapDim-1) Then
					vertNeighborCount=vertNeighborCount+(GetIntArrayElem(layout,x,y+1)<>0)
				EndIf

				If (horNeighborCount+vertNeighborCount = 1) Then
					SetIntArrayElem(layout,ROOM1,x,y)
				ElseIf ((horNeighborCount+vertNeighborCount = 3)) Then
					SetIntArrayElem(layout,ROOM3,x,y)
				ElseIf ((horNeighborCount+vertNeighborCount = 4)) Then
					SetIntArrayElem(layout,ROOM4,x,y)
				ElseIf ((horNeighborCount = 1) And (vertNeighborCount = 1)) Then
					SetIntArrayElem(layout,ROOM2C,x,y)
				ElseIf ((horNeighborCount = 2) Xor (vertNeighborCount = 2)) Then
					SetIntArrayElem(layout,ROOM2,x,y)
				Else
					SetIntArrayElem(layout,0,x,y)
				EndIf
			EndIf
		Next
	Next
End Function

Function DetermineRotation%(layout.IntArray,x%,y%)
	Select (GetIntArrayElem(layout,x,y))
		Case ROOM1
			If (x>0) And (GetIntArrayElem(layout,x-1,y)<>0) Then
				Return 270
			ElseIf ((x<layout\xDim-1) And (GetIntArrayElem(layout,x+1,y)<>0)) Then
				Return 90
			ElseIf ((y>0) And (GetIntArrayElem(layout,x,y-1)<>0)) Then
				Return 0
			Else
				Return 180
			EndIf
		Case ROOM2
			If (GetIntArrayElem(layout,x-1,y)<>0) Then
				Return 90+Rand(0,1)*180
			Else
				Return (Rand(0,1)*180)
			EndIf
		Case ROOM2C
			If (x>0) And (GetIntArrayElem(layout,x-1,y)<>0) Then
				If (y>0) And (GetIntArrayElem(layout,x,y-1)<>0) Then
					Return 270
				Else
					Return 180
				EndIf
			Else
				If (y>0) And (GetIntArrayElem(layout,x,y-1)<>0) Then
					Return 0
				Else
					Return 90
				EndIf
			EndIf
		Case ROOM3
			If (x>0) And (GetIntArrayElem(layout,x-1,y)=0) Then
				Return 90
			ElseIf ((y>0) And (GetIntArrayElem(layout,x,y-1)=0)) Then
				Return 180
			ElseIf ((x<layout\xDim-1) And (GetIntArrayElem(layout,x+1,y)=0)) Then
				Return 270
			Else
				Return 0
			EndIf
		Case ROOM4
			Return Rand(0,3)*90
		Default
			Return -1
	End Select
End Function

Function CheckRoomOverlap%(roomname$, x%, y%)
	Return False ;TODO: reimplement?
End Function

Function GetZone%(y%)
	Return -1;TODO: reimplement
End Function

;-------------------------------------------------------------------------------------------------------

Function AmbientLightRooms(value%=0)
	Local mesh%,surf%,brush%,tex0%

	If (value=AmbientLightRoomVal) Then Return
	AmbientLightRoomVal = value

	Local oldbuffer% = BackBuffer() ;probably shouldn't make assumptions here but who cares, why wouldn't it use the backbuffer ;GetBuffer()

	SetBuffer(TextureBuffer(AmbientLightRoomTex))

	ClsColor(value,value,value)
	Cls()
	ClsColor(0,0,0)

	SetBuffer(oldbuffer)
End Function

;TODO: Probably remove.
Function FindAndDeleteFakeMonitor(r.Room,x#,y#,z#,Amount%)
	Local i%

	For i = 0 To Amount
		If (r\objects[i]<>0) Then
			If (EntityX(r\objects[i],True) = x) Then
				If (EntityY(r\objects[i],True) = y) Then
					If (EntityZ(r\objects[i],True) = z) Then
						FreeEntity(r\objects[i])
						r\objects[i]=0
						DebugLog("Deleted Fake Monitor: "+Str(i))
						Exit
					EndIf
				EndIf
			EndIf
		EndIf
	Next

End Function

;~IDEal Editor Parameters:
;~C#Blitz3D