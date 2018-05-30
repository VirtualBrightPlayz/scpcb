Include "Rooms/Room_chck_hcz_ez_2.bb"
Include "Rooms/Room_chck_lcz_hcz_2.bb"
Include "Rooms/Room_closets_2.bb"
Include "Rooms/Room_cont_008_1.bb"
Include "Rooms/Room_cont_012_2.bb"
Include "Rooms/Room_cont_035_1.bb"
Include "Rooms/Room_cont_049_2.bb"
Include "Rooms/Room_cont_079_1.bb"
Include "Rooms/Room_cont_106_1.bb"
Include "Rooms/Room_cont_1123_2.bb"
Include "Rooms/Room_cont_1162_2c.bb"
Include "Rooms/Room_cont_173_1.bb"
Include "Rooms/Room_cont_205_1.bb"
Include "Rooms/Room_cont_500_1499_2.bb"
Include "Rooms/Room_cont_513_3.bb"
Include "Rooms/Room_cont_714_860_1025_2.bb"
Include "Rooms/Room_cont_895_1.bb"
Include "Rooms/Room_cont_914_1.bb"
Include "Rooms/Room_cont_966_3.bb"
Include "Rooms/Room_dimension1499.bb"
Include "Rooms/Room_end_1.bb"
Include "Rooms/Room_exit_gatea_1.bb"
Include "Rooms/Room_exit_gateb_1.bb"
Include "Rooms/Room_extend_gatea_1.bb"
Include "Rooms/Room_hll_bench_3.bb"
Include "Rooms/Room_hll_caf_2.bb"
Include "Rooms/Room_hll_dirty_3.bb"
Include "Rooms/Room_hll_ele_2.bb"
Include "Rooms/Room_hll_fan_2.bb"
Include "Rooms/Room_hll_gas_2.bb"
Include "Rooms/Room_hll_gas_3.bb"
Include "Rooms/Room_hll_lights_2.bb"
Include "Rooms/Room_hll_lshape_2.bb"
Include "Rooms/Room_hll_pipes_3.bb"
Include "Rooms/Room_hll_plain_2.bb"
Include "Rooms/Room_hll_plain_3.bb"
Include "Rooms/Room_hll_plain_4.bb"
Include "Rooms/Room_hll_sl_2.bb"
Include "Rooms/Room_hll_toilets_2.bb"
Include "Rooms/Room_intro.bb"
Include "Rooms/Room_lck_096_2c.bb"
Include "Rooms/Room_lck_air_2.bb"
Include "Rooms/Room_lck_broke_2c.bb"
Include "Rooms/Room_lck_cam_2c.bb"
Include "Rooms/Room_lck_ez_3.bb"
Include "Rooms/Room_lck_tshape_2.bb"
Include "Rooms/Room_lifts_1.bb"
Include "Rooms/Room_off_2level_2.bb"
Include "Rooms/Room_off_bain_2.bb"
Include "Rooms/Room_off_gears_may_har_2.bb"
Include "Rooms/Room_off_glss_3.bb"
Include "Rooms/Room_off_lower_2.bb"
Include "Rooms/Room_off_l_conf_2.bb"
Include "Rooms/Room_off_plain_2.bb"
Include "Rooms/Room_off_rosewood_2.bb"
Include "Rooms/Room_pocketdimension.bb"
Include "Rooms/Room_scp_970_2.bb"
Include "Rooms/Room_srvr_096_2.bb"
Include "Rooms/Room_srvr_farm_3.bb"
Include "Rooms/Room_srvr_lshape_3.bb"
Include "Rooms/Room_srvr_pc_2.bb"
Include "Rooms/Room_strg_939_3.bb"
Include "Rooms/Room_strg_elec_2c.bb"
Include "Rooms/Room_test_682_2.bb"
Include "Rooms/Room_test_860_2.bb"
Include "Rooms/Room_test_smallwindow_2.bb"
Include "Rooms/Room_tnnl_elec_2.bb"
Include "Rooms/Room_tnnl_maintenance_2.bb"
Include "Rooms/Room_tnnl_pipes_2.bb"
Include "Rooms/Room_tnnl_plain_2.bb"
Include "Rooms/Room_tnnl_plain_3.bb"
Include "Rooms/Room_tnnl_plain_4.bb"
Include "Rooms/Room_hll_tsl.bb"
Include "Rooms/Room_tnnl_nuke_2.bb"

Include "RM2.bb"

Include "Skybox.bb"

Type Materials
	Field name$
	Field Diff
	;Field Bump
	
	Field StepSound%
End Type

Function LoadMaterials(file$)
	Local TemporaryString$
	Local mat.Materials = Null
	Local StrTemp$ = ""
	
	Local f = OpenFile(file)
	
	Local stepSound$ = ""
	
	While Not Eof(f)
		TemporaryString = Trim(ReadLine(f))
		If Left(TemporaryString,1) = "[" Then
			TemporaryString = Mid(TemporaryString, 2, Len(TemporaryString) - 2)
			
			mat.Materials = New Materials
			
			mat\name = Lower(TemporaryString)
			
			mat\Diff = 0
			
			stepSound = GetINIString(file, TemporaryString, "stepsound")
			If Lower(stepSound)="metal" mat\StepSound = STEPSOUND_METAL
		EndIf
	Wend
	
	CloseFile f
	
End Function

;RMESH STUFF;;;;

Function StripFilename$(file$)
	Local mi$=""
	Local lastSlash%=0
	If Len(file)>0
		For i%=1 To Len(file)
			mi=Mid(file$,i,1)
			If mi="\" Or mi="/" Then
				lastSlash=i
			EndIf
		Next
	EndIf
	
	Return Left(file,lastSlash)
End Function

Function GetTextureFromCache%(name$)
	For tc.Materials=Each Materials
		If Lower(tc\name) = Lower(name) Then Return tc\Diff
	Next
	Return 0
End Function

Function GetCache.Materials(name$)
	For tc.Materials=Each Materials
		If Lower(tc\name) = Lower(name) Then Return tc
	Next
	Return Null
End Function

Function AddTextureToCache(name$,texture%)
	Local tc.Materials=GetCache(name)
	If tc.Materials=Null Then
		tc.Materials=New Materials
		tc\name=Lower(name)
		
		tc\Diff=0
	EndIf
	If tc\Diff=0 Then tc\Diff=texture
End Function

Function ClearTextureCache()
	For tc.Materials=Each Materials
		If tc\Diff<>0 Then FreeTexture tc\Diff
		;If tc\Bump<>0 Then FreeTexture tc\Bump
		Delete tc
	Next
End Function

Function FreeTextureCache()
	For tc.Materials=Each Materials
		If tc\Diff<>0 Then FreeTexture tc\Diff
		;If tc\Bump<>0 Then FreeTexture tc\Bump
		tc\Diff = 0; : tc\Bump = 0
	Next
End Function

Function LoadRMeshTexture%(roompath$,name$,flags%)
	Local texture% = 0
	If texture=0 Then texture = LoadTexture(roompath+name+".jpg",flags)
	If texture=0 Then texture = LoadTexture(roompath+name+".png",flags)
	If texture=0 Then texture = LoadTexture("GFX/map/Textures/"+name+".jpg",flags)
	If texture=0 Then texture = LoadTexture("GFX/map/Textures/"+name+".png",flags)
	If texture<>0 Then
		DebugLog TextureName(texture)
	Else
		RuntimeError name
	EndIf
	Return texture
End Function

;-----------;;;;

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

Function Piece$(s$,entry,char$=" ")
	While Instr(s,char+char)
		s=Replace(s,char+char,char)
	Wend
	For n=1 To entry-1
		p=Instr(s,char)
		s=Right(s,Len(s)-p)
	Next
	p=Instr(s,char)
	If p<1
		a$=s
	Else
		a=Left(s,p-1)
	EndIf
	Return a
End Function

Function KeyValue$(entity,key$,defaultvalue$="")
	properties$=EntityName(entity)
	properties$=Replace(properties$,Chr(13),"")
	key$=Lower(key)
	Repeat
		p=Instr(properties,Chr(10))
		If p Then test$=(Left(properties,p-1)) Else test=properties
		testkey$=Piece(test,1,"=")
		testkey=Trim(testkey)
		testkey=Replace(testkey,Chr(34),"")
		testkey=Lower(testkey)
		If testkey=key Then
			value$=Piece(test,2,"=")
			value$=Trim(value$)
			value$=Replace(value$,Chr(34),"")
			Return value
		EndIf
		If Not p Then Return defaultvalue$
		properties=Right(properties,Len(properties)-p)
	Forever 
End Function

Const MaxRoomLights% = 32
Const MaxRoomEmitters% = 8
Const MaxRoomObjects% = 30

Const ROOM0%=0, ROOM1% = 1, ROOM2% = 2, ROOM2C% = 3, ROOM3% = 4, ROOM4% = 5

Const ZONE_LCZ% = 1, ZONE_HCZ% = 2, ZONE_EZ% = 4

Global RoomTempID.MarkedForRemoval
Type RoomTemplates
	Field name$
	Field shape%
	Field large% ;TODO: might not be needed?
	
	Field objPath$
	Field loaded%
	
	Field opaqueMesh%
	Field alphaMesh%
	Field collisionObjs.IntArrayList
	Field props.IntArrayList
	
	Field zones%
	
	;TODO: cleanup?
	Field TempSoundEmitter%[MaxRoomEmitters]
	Field TempSoundEmitterX#[MaxRoomEmitters],TempSoundEmitterY#[MaxRoomEmitters],TempSoundEmitterZ#[MaxRoomEmitters]
	Field TempSoundEmitterRange#[MaxRoomEmitters]
	
	Field Commonness#
	Field MinAmount%, MaxAmount%
	Field xRangeStart#, xRangeEnd#
	Field yRangeStart#, yrangeEnd#
	Field DisableDecals%
	
	;TODO: remove
	Field TempTriggerboxAmount
	Field TempTriggerbox[128]
	Field TempTriggerboxName$[128]
End Type 	

Function CreateRoomTemplate.RoomTemplates(meshpath$)
	Local rt.RoomTemplates = New RoomTemplates
	
	rt\objPath = meshpath
	rt\loaded = False
	
	Return rt
End Function

Function LoadRoomTemplates(file$)
	Local TemporaryString$, i%
	Local rt.RoomTemplates = Null
	Local StrTemp$ = ""
	Local Zones$ = ""
	Local AmountRange$ = ""
	Local xRange$, yRange$
	
	Local f = OpenFile(file)
	
	While Not Eof(f)
		TemporaryString = Trim(ReadLine(f))
		If Left(TemporaryString,1) = "[" Then
			TemporaryString = Mid(TemporaryString, 2, Len(TemporaryString) - 2)
			StrTemp = GetINIString(file, TemporaryString, "meshpath")
			
			rt = CreateRoomTemplate(StrTemp)
			rt\name = Lower(TemporaryString)
			
			StrTemp = Lower(GetINIString(file, TemporaryString, "shape", "0"))
			
			Select StrTemp
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
			If Instr(Zones,"lcz")>0 Then
				rt\zones = rt\zones Or ZONE_LCZ
			EndIf
			If Instr(Zones,"hcz")>0 Then
				rt\zones = rt\zones Or ZONE_HCZ
			EndIf
			If Instr(Zones,"ez")>0 Then
				rt\zones = rt\zones Or ZONE_EZ
			EndIf
			
			If rt\shape<>ROOM0 Then
				rt\Commonness = Max(Min(GetINIFloat(file, TemporaryString, "commonness"), 100), 0)
				
				AmountRange = GetINIString(file, TemporaryString, "amount", "")
				If AmountRange="" Then
					rt\MinAmount = -1
					rt\MaxAmount = -1
				ElseIf Instr(AmountRange,"-")>0 Then
					rt\MinAmount = Int(Left(AmountRange,Instr(AmountRange,"-")))
					rt\MaxAmount = Int(Mid(AmountRange,Instr(AmountRange,"-")+1))
				Else
					rt\MinAmount = Int(AmountRange)
					rt\MaxAmount = rt\MinAmount
				EndIf
				
				rt\large = GetINIInt(file, TemporaryString, "large")
				rt\DisableDecals = GetINIInt(file, TemporaryString, "disabledecals")
				
				xRange = GetINIString(file, TemporaryString, "xrange")
				yRange = GetINIString(file, TemporaryString, "yrange")
				
				If xRange = "" Then
					xRange = "0-1"
				EndIf
				If yRange = "" Then
					yRange = "0-1"
				EndIf
				
				rt\xRangeStart = Float(Left(xRange,Instr(xRange,"-")))
				rt\xRangeEnd = Float(Mid(xRange,Instr(xRange,"-")+1))
				
				rt\yRangeStart = Float(Left(yRange,Instr(yRange,"-")))
				rt\yrangeEnd = Float(Mid(yRange,Instr(yRange,"-")+1))
			Else
				rt\MinAmount = 0
				rt\MaxAmount = 0
				rt\zones = 0
				rt\Commonness = 0
			EndIf
			
		EndIf
	Wend
	
	i = 1
	Repeat
		StrTemp = GetINIString(file, "room ambience", "ambience"+i)
		If StrTemp = "" Then Exit
		
		RoomAmbience[i]=LoadSound(StrTemp)
		i=i+1
	Forever
	
	CloseFile f
	
End Function

Function LoadRoomMesh(rt.RoomTemplates)
	LoadRM2(rt)
End Function

Const MAP_SIZE = 19
Global RoomScale# = 8.0 / 2048.0
Global ZONEAMOUNT.MarkedForRemoval

Global MapWidth.MarkedForRemoval
Global MapHeight.MarkedForRemoval

Global RoomAmbience%[20]

Global Sky%

Global HideDistance# = 15.0

;TODO: remove/replace with functions
Global SecondaryLightOn#
Global RemoteDoorOn
Global Contained106

Type Rooms
	Field zone%
	
	Field found%
	
	Field x#, y#, z#
	Field angle%
	Field RoomTemplate.RoomTemplates
	
	Field obj% ;TODO: rename
	Field opaqueMesh%
	Field alphaMesh%
	Field collisionObjs.IntArray
	Field props.IntArray
	
	Field dist#
	
	Field SoundCHN%
	
	Field fr.Forest
	
	;TODO: use arraylists for all this stuff?
	Field SoundEmitter%[MaxRoomEmitters]
	Field SoundEmitterObj%[MaxRoomEmitters]
	Field SoundEmitterRange#[MaxRoomEmitters]
	Field SoundEmitterCHN%[MaxRoomEmitters]
	
	Field Lights%[MaxRoomLights]
	Field LightIntensity#[MaxRoomLights]
	
	Field LightSprites%[MaxRoomLights]	
	
	Field Objects%[MaxRoomObjects]
	Field Levers%[11]
	Field RoomDoors.Doors[7]
	Field NPC.NPCs[12]
	Field grid.Grids
	
	Field Adjacent.Rooms[4]
	Field AdjDoor.Doors[4]
	
	Field NonFreeAble%[10]
	Field Textures%[10]
	
	Field MaxLights% = 0
	Field LightSpriteHidden%[MaxRoomLights]
	Field LightSpritesPivot%[MaxRoomLights]
	Field LightSprites2%[MaxRoomLights]
	Field LightHidden%[MaxRoomLights]
	Field LightFlicker%[MaxRoomLights]
	Field AlarmRotor%[1]
	Field AlarmRotorLight%[1]
	Field TriggerboxAmount
	Field Triggerbox[128]
	Field TriggerboxName$[128]
	Field MaxWayPointY#
End Type 

Const gridsz%=20
Type Grids
	Field grid%[gridsz*gridsz]
	Field angles%[gridsz*gridsz]
	Field Meshes%[7]
	Field Entities%[gridsz*gridsz]
	Field waypoints.WayPoints[gridsz*gridsz]
End Type

Function UpdateGrid(grid.Grids)
	;local variables
	Local tx%,ty%
	For tx% = 0 To gridsz-1
		For ty% = 0 To gridsz-1
			If grid\Entities[tx+(ty*gridsz)]<>0 Then
				If Abs(EntityY(mainPlayer\collider,True)-EntityY(grid\Entities[tx+(ty*gridsz)],True))>4.0 Then Exit
				If Abs(EntityX(mainPlayer\collider,True)-EntityX(grid\Entities[tx+(ty*gridsz)],True))<HideDistance Then
					If Abs(EntityZ(mainPlayer\collider,True)-EntityZ(grid\Entities[tx+(ty*gridsz)],True))<HideDistance Then
						ShowEntity grid\Entities[tx+(ty*gridsz)]
					Else
						HideEntity grid\Entities[tx+(ty*gridsz)]
					EndIf
				Else
					HideEntity grid\Entities[tx+(ty*gridsz)]
				EndIf
			EndIf
		Next
	Next
End Function

Function GetRoomTemplate.RoomTemplates(name$)
	name = Lower(name)
	
	Local rt.RoomTemplates
	For rt = Each RoomTemplates
		If rt\name = name Then
			Return rt
		EndIf
	Next
End Function

Function CountRooms%(rt.RoomTemplates)
	Local count% = 0
	
	Local r.Rooms
	For r = Each Rooms
		If r\RoomTemplate = rt Then count=count+1
	Next
	Return count
End Function

Function CreateRoom.Rooms(rt.RoomTemplates, x#, y#, z#)
	Local r.Rooms = New Rooms
	
	DebugLog "Placing "+rt\name
	
	; TODO: Does 'zone' exist?
	r\zone = zone
	
	r\x = x : r\y = y : r\z = z
	
	r\RoomTemplate = rt
	
	If Not rt\loaded Then LoadRoomMesh(rt)
	
	Local tempObj%
	Local tempProp.Props
	
	r\obj = CreatePivot()
	r\opaqueMesh = CopyEntity(rt\opaqueMesh) : ScaleEntity(r\opaqueMesh, RoomScale, RoomScale, RoomScale)
	EntityParent(r\opaqueMesh,r\obj)
	ShowEntity(r\opaqueMesh)
	If rt\alphaMesh<>0 Then
		r\alphaMesh = CopyEntity(rt\alphaMesh) : ScaleEntity(r\alphaMesh, RoomScale, RoomScale, RoomScale)
		ShowEntity(r\alphaMesh)
		EntityParent(r\alphaMesh,r\obj)
	EndIf
	r\collisionObjs = CreateIntArray(rt\collisionObjs\size)
	For i% = 0 To rt\collisionObjs\size-1
		tempObj = CopyEntity(GetIntArrayListElem(rt\collisionObjs,i)) : ScaleEntity(tempObj, RoomScale, RoomScale, RoomScale)
		SetIntArrayElem(r\collisionObjs,tempObj,i)
		ShowEntity(tempObj) : EntityAlpha(tempObj,0.0)
		EntityParent(tempObj,r\obj)
	Next
	If rt\props<>Null Then
		r\props = CreateIntArray(rt\props\size)
		For i% = 0 To rt\props\size-1
			tempProp = Object.Props(GetIntArrayListElem(rt\props,i))
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

Function FillRoom(r.Rooms)
	Select r\RoomTemplate\name
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
		Case "chck_lcz_hcz_2"
			FillRoom_chck_lcz_hcz_2(r)
		Case "chck_hcz_ez_2"
			FillRoom_chck_hcz_ez_2(r)
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
		Case "hll_lights_2"
			FillRoom_hll_lights_2(r)
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
	
	For lt.lighttemplates = Each LightTemplates
		If lt\roomtemplate = r\RoomTemplate Then
			newlt = AddLight(r, r\x+lt\x*RoomScale, r\y+lt\y*RoomScale, r\z+lt\z*RoomScale, lt\ltype, lt\range, lt\r, lt\g, lt\b)
			If newlt <> 0 Then 
				If lt\ltype = 3 Then
					LightConeAngles(newlt, lt\innerconeangle, lt\outerconeangle)
					RotateEntity(newlt, lt\pitch, lt\yaw, 0)
				EndIf
			EndIf
		EndIf
	Next
	
	For ts.tempscreens = Each TempScreens
		If ts\roomtemplate = r\RoomTemplate Then
			CreateScreen(r\x+ts\x*RoomScale, r\y+ts\y*RoomScale, r\z+ts\z*RoomScale, ts\imgpath, r)
		EndIf
	Next
	
	Local waypoints.IntArrayList = CreateIntArrayList()
	Local waypoint.WayPoints
	For tw.TempWayPoints = Each TempWayPoints
		If tw\roomtemplate = r\RoomTemplate Then
			waypoint = CreateWaypoint(r\x+tw\x*RoomScale, r\y+tw\y*RoomScale, r\z+tw\z*RoomScale, r)
			PushIntArrayListElem(waypoints,Handle(waypoint))
		EndIf
	Next
	
	Local i% = 0
	For tw.TempWayPoints = Each TempWayPoints
		If tw\roomtemplate = r\RoomTemplate Then
			waypoint = Object.WayPoints(GetIntArrayListElem(waypoints,i))
			For j% = 0 To 15
				If tw\connectedTo[j]=0 Then Exit
				waypoint\connected[j] = Object.WayPoints(GetIntArrayListElem(waypoints,tw\connectedTo[j]-1))
				waypoint\dist[j] = EntityDistance(waypoint\obj,waypoint\connected[j]\obj)
			Next
			i=i+1
		EndIf
	Next
	
	DeleteIntArrayList(waypoints)
	
;	If r\RoomTemplate\TempTriggerboxAmount > 0
;		r\TriggerboxAmount = r\RoomTemplate\TempTriggerboxAmount
;		For i = 0 To r\TriggerboxAmount-1
;			r\Triggerbox[i] = CopyEntity(r\RoomTemplate\TempTriggerbox[i],r\obj)
;			EntityAlpha r\Triggerbox[i],0.0
;			r\TriggerboxName[i] = r\RoomTemplate\TempTriggerboxName[i]
;			DebugLog "Triggerbox found: "+i
;			DebugLog "Triggerbox "+i+" name: "+r\TriggerboxName[i]
;		Next
;	EndIf
	
	For i = 0 To MaxRoomEmitters-1
		If r\RoomTemplate\TempSoundEmitter[i]<>0 Then
			r\SoundEmitterObj[i]=CreatePivot(r\obj)
			PositionEntity r\SoundEmitterObj[i], r\x+r\RoomTemplate\TempSoundEmitterX[i]*RoomScale,r\y+r\RoomTemplate\TempSoundEmitterY[i]*RoomScale,r\z+r\RoomTemplate\TempSoundEmitterZ[i]*RoomScale,True
			EntityParent(r\SoundEmitterObj[i],r\obj)
			
			r\SoundEmitter[i] = r\RoomTemplate\TempSoundEmitter[i]
			r\SoundEmitterRange[i] = r\RoomTemplate\TempSoundEmitterRange[i]
		EndIf
	Next
	
End Function

Function SetRoomVisibility(r.Rooms,on%)
	If on Then
		ShowEntity(r\opaqueMesh)
		If r\alphaMesh<>0 Then
			ShowEntity(r\alphaMesh)
		EndIf
	Else
		HideEntity(r\opaqueMesh)
		If r\alphaMesh<>0 Then
			HideEntity(r\alphaMesh)
		EndIf
	EndIf
End Function

Function UpdateRooms()
	Local dist#, i%, j%, r.Rooms
	
	Local x#,z#,hide%=True
	
	;PlayerZone=Min(Max(GetZone(EntityZ(mainPlayer\collider)/8.0),0),ZONEAMOUNT-1)
	
	;TempLightVolume=0
	Local foundPlayerRoom% = False
	If mainPlayer\currRoom<>Null Then
		If Abs(EntityY(mainPlayer\collider) - EntityY(mainPlayer\currRoom\obj)) < 1.5 Then
			x = Abs(mainPlayer\currRoom\x-EntityX(mainPlayer\collider,True))
			If x < 4.0 Then
				z = Abs(mainPlayer\currRoom\z-EntityZ(mainPlayer\collider,True))
				If z < 4.0 Then
					foundPlayerRoom = True
				EndIf
			EndIf
			
			If foundPlayerRoom = False Then ;it's likely that an adjacent room is the new player room, check for that
				For i=0 To 3
					If mainPlayer\currRoom\Adjacent[i]<>Null Then
						x = Abs(mainPlayer\currRoom\Adjacent[i]\x-EntityX(mainPlayer\collider,True))
						If x < 4.0 Then
							z = Abs(mainPlayer\currRoom\Adjacent[i]\z-EntityZ(mainPlayer\collider,True))
							If z < 4.0 Then
								foundPlayerRoom = True
								mainPlayer\currRoom = mainPlayer\currRoom\Adjacent[i]
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
		Local minDist# = 999.0
		For r.Rooms = Each Rooms
			x = Abs(r\x-EntityX(mainPlayer\collider,True))
			z = Abs(r\z-EntityZ(mainPlayer\collider,True))
			r\dist = Max(x,z)
			
			If r\dist<minDist Then
				mainPlayer\currRoom = r
				minDist = r\dist
			EndIf
		Next
		Return
	EndIf
	
	For r.Rooms = Each Rooms
		x = Abs(r\x-EntityX(mainPlayer\collider,True))
		z = Abs(r\z-EntityZ(mainPlayer\collider,True))
		r\dist = Max(x,z)
		
		
		If x<16 And z < 16 Then
			For i = 0 To MaxRoomEmitters-1
				If r\SoundEmitter[i]<>0 Then 
					dist# = EntityDistance(r\SoundEmitterObj[i],mainPlayer\collider)
					If dist < r\SoundEmitterRange[i] Then
						r\SoundEmitterCHN[i] = LoopRangedSound(RoomAmbience[r\SoundEmitter[i]],r\SoundEmitterCHN[i], mainPlayer\cam, r\SoundEmitterObj[i],r\SoundEmitterRange[i])
					EndIf
				EndIf
			Next
			
			If (Not foundPlayerRoom) And (mainPlayer\currRoom<>r) Then				
				If x < 4.0 Then
					If z < 4.0 Then
						If Abs(EntityY(mainPlayer\collider) - EntityY(r\obj)) < 1.5 Then mainPlayer\currRoom = r
						foundPlayerRoom = True
					EndIf
				EndIf				
			EndIf
		EndIf
		
		hide = True
		
		If r=mainPlayer\currRoom Then hide = False
		If hide Then
			If IsRoomAdjacent(mainPlayer\currRoom,r) Then hide = False
		EndIf
		If hide Then
			For i=0 To 3
				If (IsRoomAdjacent(mainPlayer\currRoom\Adjacent[i],r)) Then hide=False : Exit
			Next
		EndIf
		
		If hide Then
			HideEntity r\obj
		Else
			ShowEntity r\obj
			For i = 0 To MaxRoomLights-1
				If r\Lights[i] <> 0 Then
					dist = EntityDistance(mainPlayer\collider,r\Lights[i])
					If dist < HideDistance Then
						;TempLightVolume = TempLightVolume + r\LightIntensity[i]*r\LightIntensity[i]*((HideDistance-dist)/HideDistance)
						;ShowEntity(r\Lights[i]) 						
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
	
	If mainPlayer\currRoom<>Null Then
		SetRoomVisibility(mainPlayer\currRoom,True)
		For i=0 To 3
			If mainPlayer\currRoom\Adjacent[i]<>Null Then
				x = Abs(EntityX(mainPlayer\collider,True)-EntityX(mainPlayer\currRoom\AdjDoor[i]\frameobj,True))
				z = Abs(EntityZ(mainPlayer\collider,True)-EntityZ(mainPlayer\currRoom\AdjDoor[i]\frameobj,True))
				If mainPlayer\currRoom\AdjDoor[i]\openstate = 0 Then
					SetRoomVisibility(mainPlayer\currRoom\Adjacent[i],False)
				ElseIf (Not EntityInView(mainPlayer\currRoom\AdjDoor[i]\frameobj,mainPlayer\cam))
					SetRoomVisibility(mainPlayer\currRoom\Adjacent[i],False)
				Else
					SetRoomVisibility(mainPlayer\currRoom\Adjacent[i],True)
				EndIf
				
				For j=0 To 3
					If (mainPlayer\currRoom\Adjacent[i]\Adjacent[j]<>Null) Then
						If (mainPlayer\currRoom\Adjacent[i]\Adjacent[j]<>mainPlayer\currRoom) Then SetRoomVisibility(mainPlayer\currRoom\Adjacent[i]\Adjacent[j],False)
					EndIf
				Next
			EndIf
		Next
	EndIf
	
End Function

Function IsRoomAdjacent(this.Rooms,that.Rooms)
	If this=Null Then Return False
	If this=that Then Return True
	For i=0 To 3
		If that=this\Adjacent[i] Then Return True
	Next
	Return False
End Function

;-------------------------------------------------------------------------------------------------------

Global LightVolume.MarkedForRemoval, TempLightVolume.MarkedForRemoval
Function AddLight%(room.Rooms, x#, y#, z#, ltype%, range#, r%, g%, b%)
	Local i
	
	If room<>Null Then
		For i = 0 To MaxRoomLights-1
			If room\Lights[i]=0 Then
				room\Lights[i] = CreateLight(ltype)
				;room\LightDist[i] = range
				LightRange(room\Lights[i],range)
				LightColor(room\Lights[i],r,g,b)
				PositionEntity(room\Lights[i],x,y,z,True)
				EntityParent(room\Lights[i],room\obj)
				
				room\LightIntensity[i] = (r+g+b)/255.0/3.0
				
				room\LightSprites[i]= CreateSprite()
				PositionEntity(room\LightSprites[i], x, y, z)
				ScaleSprite(room\LightSprites[i], 0.13 , 0.13)
				EntityTexture(room\LightSprites[i], LightSpriteTex(0))
				EntityBlend (room\LightSprites[i], 3)
				
				EntityParent(room\LightSprites[i], room\obj)
				
				room\LightSpritesPivot[i] = CreatePivot()
				EntityRadius room\LightSpritesPivot[i],0.05
				PositionEntity(room\LightSpritesPivot[i], x, y, z)
				EntityParent(room\LightSpritesPivot[i], room\obj)
				
				room\LightSprites2[i] = CreateSprite()
				PositionEntity(room\LightSprites2[i], x, y, z)
				ScaleSprite(room\LightSprites2[i], 0.6, 0.6)
				EntityTexture(room\LightSprites2[i], LightSpriteTex(2))
				EntityBlend(room\LightSprites2[i], 3)
				EntityOrder(room\LightSprites2[i], -1)
				EntityColor(room\LightSprites2[i], r%, g%, b%)
				EntityParent(room\LightSprites2[i], room\obj)
				EntityFX(room\LightSprites2[i],1)
				RotateEntity(room\LightSprites2[i],0,0,Rand(360))
				SpriteViewMode(room\LightSprites2[i],1)
				room\LightSpriteHidden%[i] = True
				HideEntity room\LightSprites2[i]
				room\LightFlicker%[i] = Rand(1,10)
				
				HideEntity room\Lights[i]
				
				room\MaxLights% = room\MaxLights% + 1
				
				Return room\Lights[i]
			EndIf
		Next
	Else
		Local light%,sprite%
		light=CreateLight(ltype)
		LightRange(light,range)
		LightColor(light,r,g,b)
		PositionEntity(light,x,y,z,True)
		sprite=CreateSprite()
		PositionEntity(sprite, x, y, z)
		ScaleSprite(sprite, 0.13 , 0.13)
		EntityTexture(sprite, LightSpriteTex(0))
		EntityBlend (sprite, 3)
		Return light
	EndIf
End Function

Type LightTemplates
	Field roomtemplate.RoomTemplates
	Field ltype%
	Field x#, y#, z#
	Field range#
	Field r%, g%, b%
	
	Field pitch#, yaw#
	Field innerconeangle#, outerconeangle#
End Type 

Const LIGHTTYPE_POINT% = 2
Const LIGHTTYPE_SPOT% = 3

Function AddTempLight.LightTemplates(rt.RoomTemplates, x#, y#, z#, ltype%, range#, r%, g%, b%)
	Local lt.LightTemplates = New LightTemplates
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

Type TempWayPoints
	Field x#, y#, z#
	Field connectedTo%[32]
	Field roomtemplate.RoomTemplates
End Type 

Type WayPoints
	Field obj
	Field room.Rooms
	Field state%
	;Field tempDist#
	;Field tempSteps%
	Field connected.WayPoints[16]
	Field dist#[16]
	
	Field Fcost#, Gcost#, Hcost#
	
	Field parent.WayPoints
End Type

Function CreateWaypoint.WayPoints(x#,y#,z#,room.Rooms)
	Local w.WayPoints = New WayPoints
	
	w\obj = CreatePivot()
	PositionEntity w\obj, x,y,z	
	
	EntityParent w\obj, room\obj
	
	w\room = room
	
	Return w
End Function

Function InitWayPoints(loadingstart=45)
	
	Local d.Doors, w.WayPoints, w2.WayPoints, r.Rooms, ClosestRoom.Rooms
	
	Local x#, y#, z#
	
	Local temper% = MilliSecs()
	
	Local dist#, dist2#
	
	For w.WayPoints = Each WayPoints
		EntityPickMode w\obj, 0, 0
		EntityRadius w\obj, 0
		
		For i = 0 To 4
			If w\connected[i]<>Null Then 
				Local tline = CreateLine(EntityX(w\obj,True),EntityY(w\obj,True),EntityZ(w\obj,True),EntityX(w\connected[i]\obj,True),EntityY(w\connected[i]\obj,True),EntityZ(w\connected[i]\obj,True))
				EntityColor(tline, 255,0,0)
				EntityParent tline, w\obj
			EndIf
		Next
	Next
	
	DebugLog "InitWaypoints() - "+(TimeInPosMilliSecs()-temper)
	
End Function

Function RemoveWaypoint(w.WayPoints)
	FreeEntity w\obj
	Delete w
End Function

Function FindPath(n.NPCs, x#, y#, z#)
	
	DebugLog "findpath: "+n\npcType
	
	Local temp%, dist#, dist2#
	Local xtemp#, ytemp#, ztemp#
	
	Local w.WayPoints, StartPoint.WayPoints, EndPoint.WayPoints   
	
	Local StartX% = Floor(EntityX(n\collider,True) / 8.0 + 0.5), StartZ% = Floor(EntityZ(n\collider,True) / 8.0 + 0.5)
       ;If StartX < 0 Or StartX > MapWidth Then Return 2
       ;If StartZ < 0 Or StartZ > MapWidth Then Return 2
	
	Local EndX% = Floor(x / 8.0 + 0.5), EndZ% = Floor(z / 8.0 + 0.5)
       ;If EndX < 0 Or EndX > MapWidth Then Return 2
       ;If EndZ < 0 Or EndZ > MapWidth Then Return 2
	
	Local CurrX, CurrZ
	
       ;pathstatus = 0, ei ole etsitty reitti�
       ;pathstatus = 1, reitti l�ydetty
       ;pathstatus = 2, reitti� ei ole olemassa   
	
	For w.WayPoints = Each WayPoints
		w\state = 0
		w\Fcost = 0
		w\Gcost = 0
		w\Hcost = 0
	Next
	
	n\pathStatus = 0
	n\pathLocation = 0
	
	Local i%
	For i = 0 To 19
		n\path[i] = Null
	Next
	
	Local pvt = CreatePivot()
	PositionEntity(pvt, x,y,z, True)   
	
	temp = CreatePivot()
	PositionEntity(temp, EntityX(n\collider,True), EntityY(n\collider,True)+0.15, EntityZ(n\collider,True))
	
	dist = 350.0
	For w.WayPoints = Each WayPoints
		xtemp = EntityX(w\obj,True)-EntityX(temp,True)
          ;If xtemp < 8.0 Then
		ztemp = EntityZ(w\obj,True)-EntityZ(temp,True)
             ;If ztemp < 8.0 Then
		ytemp = EntityY(w\obj,True)-EntityY(temp,True)
                ;If ytemp < 8.0 Then
		dist2# = (xtemp*xtemp)+(ytemp*ytemp)+(ztemp*ztemp)
		If dist2 < dist Then 
			;prefer waypoints that are visible
			If Not EntityVisible(w\obj, temp) Then dist2 = dist2*3
			If dist2 < dist Then 
				dist = dist2
				StartPoint = w
			EndIf
		EndIf
                ;EndIf
             ;EndIf
          ;EndIf
	Next
	DebugLog "DIST: "+dist
	
	FreeEntity temp
	
	If StartPoint = Null Then Return 2
	StartPoint\state = 1      
	
       ;If EndPoint = Null Then
	EndPoint = Null
	dist# = 400.0
	For w.WayPoints = Each WayPoints
		xtemp = EntityX(pvt,True)-EntityX(w\obj,True)
          ;If xtemp =< 8.0 Then
		ztemp = EntityZ(pvt,True)-EntityZ(w\obj,True)
             ;If ztemp =< 8 Then
		ytemp = EntityY(pvt,True)-EntityY(w\obj,True)
		dist2# = (xtemp*xtemp)+(ytemp*ytemp)+(ztemp*ztemp)
		
		If dist2 < dist Then ; And EntityVisible(w\obj, pvt)
			dist = dist2
			EndPoint = w
		EndIf            
             ;EndIf
          ;EndIf
	Next
       ;EndIf
	
	FreeEntity pvt
	
	If EndPoint = StartPoint Then
		If dist < 0.4 Then
			Return 0
		Else
			n\path[0]=EndPoint
			Return 1               
		EndIf
	EndIf
	If EndPoint = Null Then Return 2
	
       ;aloitus- ja lopetuspisteet l�ydetty, aletaan etsi� reitti�
	
	Repeat
		
		temp% = False
		Local smallest.WayPoints = Null
		dist# = 10000.0
		For w.WayPoints = Each WayPoints
			If w\state = 1 Then
                temp = True
                If (w\Fcost) < dist Then
					dist = w\Fcost
					smallest = w
                EndIf
			EndIf
		Next
		
		If smallest <> Null Then
			
			w = smallest
			w\state = 2
			
			For i = 0 To 15
                If w\connected[i]<>Null Then
					If w\connected[i]\state < 2 Then
						
						If w\connected[i]\state=1 Then ;open list
							Local gtemp# = w\Gcost+w\dist[i]
							;TODO: fix?
							;If n\npcType = NPCtypeMTF Then
							;	If w\connected[i]\door = Null Then gtemp = gtemp + 0.5
							;EndIf
							If gtemp < w\connected[i]\Gcost Then ;parempi reitti -> overwrite
								w\connected[i]\Gcost = gtemp
								w\connected[i]\Fcost = w\connected[i]\Gcost + w\connected[i]\Hcost
								w\connected[i]\parent = w
							EndIf
						Else
							w\connected[i]\Hcost# = Abs(EntityX(w\connected[i]\obj,True)-EntityX(EndPoint\obj,True))+Abs(EntityZ(w\connected[i]\obj,True)-EntityZ(EndPoint\obj,True))
							gtemp# = w\Gcost+w\dist[i]
							;TODO: fix?
							;If n\npcType = NPCtypeMTF Then
							;	If w\connected[i]\door = Null Then gtemp = gtemp + 0.5
							;EndIf
							w\connected[i]\Gcost = gtemp
							w\connected[i]\Fcost = w\Gcost+w\Hcost
							w\connected[i]\parent = w
							w\connected[i]\state=1
						EndIf            
					EndIf
					
                EndIf
			Next
		Else ;open listilt� ei l�ytynyt mit��n
			If EndPoint\state > 0 Then
                StartPoint\parent = Null
                EndPoint\state = 2
                Exit
			EndIf
		EndIf
		
		If EndPoint\state > 0 Then
			StartPoint\parent = Null
			EndPoint\state = 2
			Exit
		EndIf
		
	Until temp = False
	
	If EndPoint\state > 0 Then
		
		currpoint.waypoints = EndPoint
		twentiethpoint.waypoints = EndPoint
		
		length = 0
		Repeat
			length = length +1
			currpoint = currpoint\parent
			If length>20 Then
                twentiethpoint = twentiethpoint\parent
			EndIf
		Until currpoint = Null
		
		currpoint.waypoints = EndPoint
		While twentiethpoint<>Null
			length=Min(length-1,19)
             ;DebugLog "LENGTH "+length
			twentiethpoint = twentiethpoint\parent
			n\path[length] = twentiethpoint
		Wend
		
		Return 1
          ;RuntimeError length
    ;      For i = 0 To (length-1)
    ;         temp =False
    ;         If length < 20 Then
    ;            n\Path[length-1-i] = currpoint.WayPoints
    ;         Else
    ;            If i < 20 Then
    ;               n\Path[20-1-i] = w.WayPoints
    ;            Else
    ;               ;Return 1
    ;            EndIf
    ;         EndIf
    ;         
    ;         If currpoint = StartPoint Then Return 1
    ;         
    ;         If currpoint\parent <> Null Then
    ;            currpoint = currpoint\parent
    ;         Else
    ;            Exit
    ;         EndIf
    ;         
    ;      Next
		
	Else
		
		DebugLog "FUNCTION FindPath() - reitti� ei l�ytynyt"
		Return 2 ;reitti� m��r�np��h�n ei l�ytynyt
		
	EndIf
	
End Function

Function CreateLine(x1#,y1#,z1#, x2#,y2#,z2#, mesh=0)
	Local surf, verts
	
	If mesh = 0 Then 
		mesh=CreateMesh()
		EntityFX(mesh,16)
		surf=CreateSurface(mesh)	
		verts = 0	
		
		AddVertex surf,x1#,y1#,z1#,0,0
	Else
		surf = GetSurface(mesh,1)
		verts = CountVertices(surf)-1
	End If
	
	AddVertex surf,(x1#+x2#)/2,(y1#+y2#)/2,(z1#+z2#)/2,0,0 
	; you could skip creating the above vertex and change the line below to
	; AddTriangle surf,verts,verts+1,verts+0
	; so your line mesh would use less vertices, the drawback is that some videocards (like the matrox g400)
	; aren't able to create a triangle with 2 vertices. so, it's your call :)
	AddVertex surf,x2#,y2#,z2#,1,0
	
	AddTriangle surf,verts,verts+2,verts+1
	
	Return mesh
End Function

;-------------------------------------------------------------------------------------------------------

Global SelectedScreen.Screens
Type Screens
	Field obj%
	Field imgpath$
	Field img
	Field room.Rooms
End Type

Type TempScreens
	Field imgpath$
	Field x#,y#,z#
	Field roomtemplate.RoomTemplates
End Type

Function CreateScreen.Screens(x#,y#,z#,imgpath$,r.Rooms)
	Local s.Screens = New Screens
	s\obj = CreatePivot()
	EntityPickMode(s\obj, 1)	
	EntityRadius s\obj, 0.1
	
	PositionEntity s\obj, x,y,z
	s\imgpath = imgpath
	s\room = r
	EntityParent s\obj, r\obj
	
	Return s
End Function

Function UpdateScreens()
	If SelectedScreen <> Null Then Return
	If SelectedDoor <> Null Then Return
	
	Local s.Screens
	For s = Each Screens
		If s\room = mainPlayer\currRoom Then
			If EntityDistance(mainPlayer\collider,s\obj)<1.2 Then
				EntityPick(mainPlayer\cam, 1.2)
				If PickedEntity()=s\obj And s\imgpath<>"" Then
					DrawHandIcon=True
					If MouseUp1 Then 
						SelectedScreen=s
						s\img = LoadImage("GFX\screens\"+s\imgpath)
						s\img = ResizeImage2(s\img, ImageWidth(s\img) * MenuScale, ImageHeight(s\img) * MenuScale)
						MaskImage s\img, 255,0,255
						PlaySound_SM(sndManager\button)
						MouseUp1=False
					EndIf
				EndIf
			EndIf
		EndIf
	Next
	
End Function

;-------------------------------------------------------------------------------------------------------


Dim GorePics%(10)
Global SelectedMonitor.SecurityCams
Global CoffinCam.SecurityCams
Type SecurityCams
	Field obj%, MonitorObj%
	
	Field BaseObj%, CameraObj%
	
	Field ScrObj%, ScrWidth#, ScrHeight#
	Field Screen%, Cam%, ScrTexture%, ScrOverlay%
	Field angle#, turn#, CurrAngle#
	Field State#, PlayerState%
	
	Field soundCHN%
	
	Field InSight%
	
	Field RenderInterval#
	
	Field room.Rooms
	
	Field FollowPlayer%
	Field CoffinEffect%
	
	Field AllowSaving%
	
	Field MinAngle#, MaxAngle#, dir%
	
	Field IsRoom2slCam% = False
	Field Room2slTexs%[2]
	Field SpecialCam% = False
	Field ID% = -1
End Type

Global ScreenTexs%[2]

Function CreateSecurityCam.SecurityCams(x#, y#, z#, r.Rooms, screen% = False)
	Local sc.SecurityCams = New SecurityCams
	
	sc\obj = CopyEntity(CamBaseOBJ)
	ScaleEntity(sc\obj, 0.0015, 0.0015, 0.0015)
	sc\CameraObj = CopyEntity(CamOBJ)
	ScaleEntity(sc\CameraObj, 0.01, 0.01, 0.01)
	
	sc\room = r
	
	sc\Screen = screen
	If screen Then
		sc\AllowSaving = True
		
		sc\RenderInterval = 12
		
		Local scale# = RoomScale * 4.5 * 0.4
		
		sc\ScrObj = CreateSprite()
		EntityFX sc\ScrObj, 17
		SpriteViewMode(sc\ScrObj, 2)
		sc\ScrTexture = 0
		EntityTexture sc\ScrObj, ScreenTexs[sc\ScrTexture]
		ScaleSprite(sc\ScrObj, MeshWidth(Monitor) * scale * 0.95 * 0.5, MeshHeight(Monitor) * scale * 0.95 * 0.5)
		
		sc\ScrOverlay = CreateSprite(sc\ScrObj)
		;	scaleSprite(sc\scrOverlay , 0.5, 0.4)
		ScaleSprite(sc\ScrOverlay, MeshWidth(Monitor) * scale * 0.95 * 0.5, MeshHeight(Monitor) * scale * 0.95 * 0.5)
		MoveEntity(sc\ScrOverlay, 0, 0, -0.0005)
		EntityTexture(sc\ScrOverlay, MonitorTexture)
		SpriteViewMode(sc\ScrOverlay, 2)
		EntityBlend(sc\ScrOverlay , 3)
		
		sc\MonitorObj = CopyEntity(Monitor, sc\ScrObj)
		
		ScaleEntity(sc\MonitorObj, scale, scale, scale)
		
		sc\Cam = CreateCamera()
		CameraViewport(sc\Cam, 0, 0, 512, 512)
		CameraRange sc\Cam, 0.05, 6.0
		CameraZoom(sc\Cam, 0.8)
		HideEntity(sc\Cam)	
	End If
	
	PositionEntity(sc\obj, x, y, z)
	
	If r<>Null Then EntityParent(sc\obj, r\obj)
	
	Return sc
End Function

Function UpdateSecurityCams()
	Local sc.SecurityCams
	
	PlayerDetected = False
	
	;coffineffect = 0, not affected by 895
	;coffineffect = 1, constantly affected by 895
	;coffineffect = 2, 079 can broadcast 895 feed on this screen
	;coffineffect = 3, 079 broadcasting 895 feed
	
	For sc.SecurityCams = Each SecurityCams
		Local close = False
		If sc\room = Null And (Not sc\SpecialCam) Then
			HideEntity sc\Cam
		Else
			If (Not sc\SpecialCam)
				If sc\room\dist < 6.0 Or mainPlayer\currRoom=sc\room Then 
					close = True
				ElseIf sc\IsRoom2slCam
					close = True
				ElseIf sc\Cam<>0
					HideEntity sc\Cam
				EndIf
			EndIf
			
			If sc\IsRoom2slCam Then sc\CoffinEffect = 0
			If sc\room <> Null
				If sc\room\RoomTemplate\name$ = "hll_sl_2" Then sc\CoffinEffect = 0
			EndIf
			If sc\SpecialCam Then sc\CoffinEffect = 0
			
			If close Or sc=CoffinCam Or sc\IsRoom2slCam Then 
				If sc\FollowPlayer Then
					If sc<>CoffinCam
						If EntityVisible(sc\CameraObj,mainPlayer\cam)
							PlayerDetected = True
						EndIf
					EndIf
					PointEntity(sc\CameraObj, mainPlayer\cam)
					Local temp# = EntityPitch(sc\CameraObj)
					RotateEntity(sc\obj, 0, CurveAngle(EntityYaw(sc\CameraObj), EntityYaw(sc\obj), 75.0), 0)
					
					If temp < 40.0 Then temp = 40
					If temp > 70.0 Then temp = 70
					RotateEntity(sc\CameraObj, CurveAngle(temp, EntityPitch(sc\CameraObj), 75.0), EntityYaw(sc\obj), 0)
					
					PositionEntity(sc\CameraObj, EntityX(sc\obj, True), EntityY(sc\obj, True) - 0.083, EntityZ(sc\obj, True))
					RotateEntity(sc\CameraObj, EntityPitch(sc\CameraObj), EntityYaw(sc\obj), 0)
				Else
					If sc\turn > 0 Then
						If sc\dir = 0 Then
							sc\CurrAngle=sc\CurrAngle+0.2 * timing\tickDuration
							If sc\CurrAngle > (sc\turn * 1.3) Then sc\dir = 1
						Else
							sc\CurrAngle=sc\CurrAngle-0.2 * timing\tickDuration
							If sc\CurrAngle < (-sc\turn * 1.3) Then sc\dir = 0
						End If
					End If
					RotateEntity(sc\obj, 0, sc\room\angle + sc\angle + Max(Min(sc\CurrAngle, sc\turn), -sc\turn), 0)
					
					PositionEntity(sc\CameraObj, EntityX(sc\obj, True), EntityY(sc\obj, True) - 0.083, EntityZ(sc\obj, True))
					RotateEntity(sc\CameraObj, EntityPitch(sc\CameraObj), EntityYaw(sc\obj), 0)
					
					If sc\Cam<>0 Then 
						PositionEntity(sc\Cam, EntityX(sc\CameraObj, True), EntityY(sc\CameraObj, True), EntityZ(sc\CameraObj, True))
						RotateEntity(sc\Cam, EntityPitch(sc\CameraObj), EntityYaw(sc\CameraObj), 0)
						MoveEntity(sc\Cam, 0, 0, 0.1)
					EndIf
					
					If sc<>CoffinCam
						If (Abs(DeltaYaw(sc\CameraObj,mainPlayer\cam))<60.0)
							If EntityVisible(sc\CameraObj,mainPlayer\cam)
								PlayerDetected = True
							EndIf
						EndIf
					EndIf
				EndIf
			EndIf
			
			If close = True Or sc\IsRoom2slCam Or sc\SpecialCam Then
				If sc\Screen Then 
					sc\State = sc\State+timing\tickDuration
					
					If sc\InSight And sc\AllowSaving Then 
						If SelectedDifficulty\saveType = SAVEONSCREENS And EntityDistance(mainPlayer\cam, sc\ScrObj)<1.0 Then
							DrawHandIcon = True
							If MouseHit1 Then SelectedMonitor = sc
						Else If SelectedMonitor = sc
							SelectedMonitor = Null
						EndIf
					Else
						SelectedMonitor = Null
					EndIf
					
					If sc\State >= sc\RenderInterval Then
						sc\InSight = False
						If mainPlayer\blinkTimer > - 5 And EntityInView(sc\ScrObj, mainPlayer\cam) Then
							If EntityVisible(mainPlayer\cam,sc\ScrObj) Then
								sc\InSight = True
								
								If sc\CoffinEffect=1 Or sc\CoffinEffect=3 Then
									If mainPlayer\blinkTimer > - 5 Then mainPlayer\sanity895=mainPlayer\sanity895-(timing\tickDuration * 16)
									
									If mainPlayer\sanity895 < (-1000) Then 
										DeathMSG = Chr(34)+"What we know is that he died of cardiac arrest. My guess is that it was caused by SCP-895, although it has never been observed affecting video equipment from this far before. "
										DeathMSG = DeathMSG + "Further testing is needed to determine whether SCP-895's "+Chr(34)+"Red Zone"+Chr(34)+" is increasing."+Chr(34)
										
										Kill(mainPlayer)				
									EndIf
								End If
								
								If (Not sc\IsRoom2slCam)
									If (Not sc\SpecialCam)
										If CoffinCam = Null Or Rand(5)=5 Or sc\CoffinEffect <> 3 Then
											HideEntity(mainPlayer\cam)
											ShowEntity(sc\Cam)
											Cls
											
											SetBuffer BackBuffer()
											RenderWorld
											CopyRect 0,0,512,512,0,0,BackBuffer(),TextureBuffer(ScreenTexs[sc\ScrTexture])
											
											HideEntity(sc\Cam)
											ShowEntity(mainPlayer\cam)										
										Else
											HideEntity(mainPlayer\cam)
											ShowEntity (CoffinCam\room\obj)
											SetRoomVisibility(CoffinCam\room,True)
											ShowEntity(CoffinCam\Cam)
											Cls
											
											SetBuffer BackBuffer()
											RenderWorld
											CopyRect 0,0,512,512,0,0,BackBuffer(),TextureBuffer(ScreenTexs[sc\ScrTexture])
											
											HideEntity (CoffinCam\room\obj)
											HideEntity(CoffinCam\Cam)
											ShowEntity(mainPlayer\cam)										
										EndIf
									Else
										HideEntity(mainPlayer\cam)
										ShowEntity(sc\Cam)
										Cls
										
										RenderWorld
										
										HideEntity(sc\Cam)
										ShowEntity(mainPlayer\cam)	
										
										CopyRect(0,0,512,512,0,0,BackBuffer(),TextureBuffer(sc\Room2slTexs[sc\ScrTexture]))
										
									EndIf
								Else
									HideEntity(mainPlayer\cam)
									ShowEntity (sc\room\obj)
									SetRoomVisibility(sc\room,True)
									ShowEntity(sc\Cam)
									Cls
									
									RenderWorld
									
									HideEntity (sc\room\obj)
									HideEntity(sc\Cam)
									ShowEntity(mainPlayer\cam)	
									
									CopyRect(0, 0, userOptions\screenWidth, userOptions\screenHeight, 0, 0, BackBuffer(), TextureBuffer(sc\Room2slTexs[sc\ScrTexture]))
								EndIf
								
							EndIf
						EndIf
						sc\State = 0
					End If
					
					If SelectedMonitor = sc Or sc\CoffinEffect=1 Or sc\CoffinEffect=3 Then
						If sc\InSight Then
						;If (Not NoClip) Then 
							Local pvt% = CreatePivot()
							PositionEntity pvt, EntityX(mainPlayer\cam), EntityY(mainPlayer\cam), EntityZ(mainPlayer\cam)
							PointEntity(pvt, sc\ScrObj)
							
							DebugLog("curvea: "+CurveAngle(EntityYaw(pvt), EntityYaw(mainPlayer\collider), Min(Max(15000.0 / (-mainPlayer\sanity895), 20.0), 200.0)))
							RotateEntity(mainPlayer\collider, EntityPitch(mainPlayer\collider), CurveAngle(EntityYaw(pvt), EntityYaw(mainPlayer\collider), Min(Max(15000.0 / (-mainPlayer\sanity895), 20.0), 200.0)), 0)
							
							TurnEntity(pvt, 90, 0, 0)
							mainPlayer\headPitch = CurveAngle(EntityPitch(pvt), mainPlayer\headPitch + 90.0, Min(Max(15000.0 / (-mainPlayer\sanity895), 20.0), 200.0))
							mainPlayer\headPitch=mainPlayer\headPitch-90						
							
							DebugLog("pvt: "+EntityYaw(pvt)+"   - coll: "+EntityYaw(mainPlayer\collider))
							
							
							FreeEntity pvt
						;EndIf
							If sc\CoffinEffect=1 Or sc\CoffinEffect=3 Then
								If mainPlayer\sanity895 < - 800 Then
									If Rand(3) = 1 Then EntityTexture(sc\ScrOverlay, MonitorTexture)
									If Rand(6) < 5 Then
										EntityTexture(sc\ScrOverlay, GorePics(Rand(0, 5)))
										;If sc\PlayerState = 1 Then PlaySound(HorrorSFX(1)) ;TODO: fix
										sc\PlayerState = 2
										If sc\soundCHN = 0 Then
											;sc\soundCHN = PlaySound(HorrorSFX(4)) ;TODO: fix
										Else
											;If Not IsChannelPlaying(sc\soundCHN) Then sc\soundCHN = PlaySound(HorrorSFX(4)) ;TODO: fix
										End If
										If sc\CoffinEffect=3 And Rand(200)=1 Then sc\CoffinEffect=2 : sc\PlayerState = Rand(10000, 20000)
									End If	
									mainPlayer\blurTimer = 1000
								ElseIf mainPlayer\sanity895 < - 500
									If Rand(7) = 1 Then EntityTexture(sc\ScrOverlay, MonitorTexture)
									If Rand(50) = 1 Then
										EntityTexture(sc\ScrOverlay, GorePics(Rand(0, 5)))
										;If sc\PlayerState = 0 Then PlaySound(HorrorSFX(0)) ;TODO: fix
										sc\PlayerState = Max(sc\PlayerState, 1)
										If sc\CoffinEffect=3 And Rand(100)=1 Then sc\CoffinEffect=2 : sc\PlayerState = Rand(10000, 20000)
									End If
								Else
									EntityTexture(sc\ScrOverlay, MonitorTexture)
								EndIf
							EndIf
						EndIf
					EndIf 
					
					If sc\InSight And sc\CoffinEffect=0 Or sc\CoffinEffect=2 Then
						If sc\PlayerState = 0 Then
							sc\PlayerState = Rand(60000, 65000)
						EndIf
						
						If Rand(500) = 1 Then
							EntityTexture(sc\ScrOverlay, OldAiPics(0))
						End If
						
						If (TimeInPosMilliSecs() Mod sc\PlayerState) >= Rand(600) Then
							EntityTexture(sc\ScrOverlay, MonitorTexture)
						Else
							If sc\soundCHN = 0 Then
								sc\soundCHN = PlaySound(LoadTempSound("SFX\SCP\079\Broadcast"+Rand(1,3)+".ogg"))
								If sc\CoffinEffect=2 Then sc\CoffinEffect=3 : sc\PlayerState = 0
							ElseIf (Not IsChannelPlaying(sc\soundCHN))
								sc\soundCHN = PlaySound(LoadTempSound("SFX\SCP\079\Broadcast"+Rand(1,3)+".ogg"))
								If sc\CoffinEffect=2 Then sc\CoffinEffect=3 : sc\PlayerState = 0
							EndIf
							EntityTexture(sc\ScrOverlay, OldAiPics(0))
						EndIf
						
					EndIf
					
				EndIf ;if screen=true
				
				If (Not sc\InSight) Then sc\soundCHN = LoopRangedSound_SM(sndManager\camera, sc\soundCHN, mainPlayer\cam, sc\CameraObj, 4.0)
			Else
				If SelectedMonitor=sc Then SelectedMonitor=Null
			EndIf
		EndIf
	Next
	
	Cls
	
	
End Function


Function UpdateLever(obj, locked=False)
	
	Local dist# = EntityDistance(mainPlayer\cam, obj)
	If dist < 8.0 Then 
		If dist < 0.8 And (Not locked) Then 
			If EntityInView(obj, mainPlayer\cam) Then 
				
				EntityPick(mainPlayer\cam, 0.65)
				
				If PickedEntity() = obj Then
					DrawHandIcon = True
					If MouseHit1 Then mainPlayer\grabbedEntity = obj
				End If
				
				prevpitch# = EntityPitch(obj)
				
				If (MouseDown1 Or MouseHit1) Then
					If mainPlayer\grabbedEntity <> 0 Then
						If mainPlayer\grabbedEntity = obj Then
							DrawHandIcon = True 
							;TurnEntity(obj, , 0, 0)
							RotateEntity(mainPlayer\grabbedEntity, Max(Min(EntityPitch(obj)+Max(Min(mouse_y_speed_1 * 8,30.0),-30), 80), -80), EntityYaw(obj), 0)
							
							DrawArrowIcon(0) = True
							DrawArrowIcon(2) = True
							
						EndIf
					EndIf
				EndIf 
				
				If EntityPitch(obj,True) > 75 Then ;p��ll�
					If prevpitch =< 75 Then PlayRangedSound_SM(sndManager\lever, mainPlayer\cam, obj, 1.0)
				ElseIf EntityPitch(obj,True) < -75 ;pois p��lt�
					If prevpitch => -75 Then PlayRangedSound_SM(sndManager\lever, mainPlayer\cam, obj, 1.0)	
				EndIf						
			EndIf
		EndIf
		
		If MouseDown1=False And MouseHit1=False Then 
			If EntityPitch(obj,True) > 0 Then
				RotateEntity(obj, CurveValue(80, EntityPitch(obj), 10), EntityYaw(obj), 0)
			Else
				RotateEntity(obj, CurveValue(-80, EntityPitch(obj), 10), EntityYaw(obj), 0)
			EndIf
			mainPlayer\grabbedEntity = 0
		End If
		
	EndIf
	
	If EntityPitch(obj,True) > 0 Then ;p��ll�
		Return True
	Else ;pois p��lt�
		Return False
	EndIf	
	
End Function

Function UpdateButton(obj)
	Local dist# = EntityDistance(mainPlayer\collider, obj);entityDistance(collider, d\buttons[i])
	If dist < 0.8 Then
		Local temp% = CreatePivot()
		PositionEntity temp, EntityX(mainPlayer\cam), EntityY(mainPlayer\cam), EntityZ(mainPlayer\cam)
		PointEntity temp,obj
		
		If EntityPick(temp, 0.65) = obj Then
			If mainPlayer\closestButton = 0 Then 
				mainPlayer\closestButton = obj
			Else
				If dist < EntityDistance(mainPlayer\collider, mainPlayer\closestButton) Then mainPlayer\closestButton = obj
			End If							
		End If
		
		FreeEntity temp
	EndIf			
	
End Function

;TODO: rewrite elevator code, use only one function
Function UpdateElevators#(State#, door1.Doors, door2.Doors, room1, room2, event.Events)
	Local x#, z#, n.NPCs, NPC_inside.NPCs
	
	door1\IsElevatorDoor = 1
	door2\IsElevatorDoor = 1
	If door1\open = True And door2\open = False Then 
		State = -1
		If (mainPlayer\closestButton = door2\buttons[0] Or mainPlayer\closestButton = door2\buttons[1]) And MouseHit1 Then
			UseDoor(door1,False)
		EndIf
		If door2\NPCCalledElevator = True
			UseDoor(door1,False)
			door2\NPCCalledElevator = 2
		EndIf
	ElseIf door2\open = True And door1\open = False
		State = 1
		If (mainPlayer\closestButton = door1\buttons[0] Or mainPlayer\closestButton = door1\buttons[1]) And MouseHit1 Then
			UseDoor(door2,False)
		EndIf
		If door1\NPCCalledElevator = True
			UseDoor(door2,False)
			door1\NPCCalledElevator = 2
		EndIf
	ElseIf Abs(door1\openstate-door2\openstate)<0.2 Then
		door1\IsElevatorDoor = 2
		door2\IsElevatorDoor = 2
	EndIf
	
	Local inside = False
	NPC_inside = Null
	
	;molemmat ovet kiinni = hissi liikkuu
	If door1\open = False And door2\open = False Then
		door1\locked = True 
		door2\locked = True
		door1\NPCCalledElevator = 2
		door2\NPCCalledElevator = 2
		If State < 0 Then ;ylh��lt� alas
			State = State - timing\tickDuration
			;pelaaja hissin sis�ll�
			If Abs(EntityX(mainPlayer\collider)-EntityX(room1,True))<280.0*RoomScale Then
				If Abs(EntityZ(mainPlayer\collider)-EntityZ(room1,True))<280.0*RoomScale Then
					If Abs(EntityY(mainPlayer\collider)-EntityY(room1,True))<280.0*RoomScale Then
						inside = True
						
						If (Not IsChannelPlaying(event\soundChannels[0])) Then
							event\soundChannels[0] = PlaySound_SM(sndManager\elevatorMove)
						EndIf
						
						mainPlayer\camShake = Sin(Abs(State)/3.0)*0.3
					EndIf
				EndIf
			EndIf
			
			For n.NPCs = Each NPCs
				If n\CanUseElevator
					If Abs(EntityX(n\collider)-EntityX(room1,True))<280.0*RoomScale
						If Abs(EntityZ(n\collider)-EntityZ(room1,True))<280.0*RoomScale Then
							If Abs(EntityY(n\collider)-EntityY(room1,True))<280.0*RoomScale Then
								NPC_inside = n
							EndIf
						EndIf
					EndIf
				EndIf
			Next
			
			If NPC_inside <> Null And (Not inside)
				NPC_inside\Idle = True
			ElseIf NPC_inside <> Null And inside
				NPC_inside\Idle = False
			EndIf
			
			If State < -400 Then
				door1\locked = False
				door2\locked = False
				door1\NPCCalledElevator = False
				door2\NPCCalledElevator = False
				State = 0
				
				If inside Then
					x# = Max(Min((EntityX(mainPlayer\collider)-EntityX(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					z# = Max(Min((EntityZ(mainPlayer\collider)-EntityZ(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					PositionEntity(mainPlayer\collider, EntityX(room2,True)+x,0.1+EntityY(room2,True)+(EntityY(mainPlayer\collider)-EntityY(room1,True)),EntityZ(room2,True)+z,True)
					ResetEntity mainPlayer\collider	
					UpdateDoorsTimer = 0
					mainPlayer\dropSpeed = 0
					UpdateDoors()
					UpdateRooms()
				EndIf
				
				If NPC_inside <> Null
					x# = Max(Min((EntityX(NPC_inside\collider)-EntityX(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					z# = Max(Min((EntityZ(NPC_inside\collider)-EntityZ(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					PositionEntity(NPC_inside\collider, EntityX(room2,True)+x,0.1+EntityY(room2,True)+(EntityY(NPC_inside\collider)-EntityY(room1,True)),EntityZ(room2,True)+z,True)
					ResetEntity NPC_inside\collider	
					UpdateDoorsTimer = 0
					NPC_inside\dropSpeed = 0
					If NPC_inside\Idle
						TurnEntity NPC_inside\obj,0,180,0
						TurnEntity NPC_inside\collider,0,180,0
						NPC_inside\Idle = False
					EndIf
					NPC_inside\CurrElevator = Null
				EndIf
				
				UseDoor(door2,False)
				
				PlayRangedSound_SM(sndManager\elevatorBeep, mainPlayer\cam, room2, 4.0)
			EndIf
		Else ;alhaalta yl�s
			State = State + timing\tickDuration
			;pelaaja hissin sis�ll�
			If Abs(EntityX(mainPlayer\collider)-EntityX(room2,True))<280.0*RoomScale Then
				If Abs(EntityZ(mainPlayer\collider)-EntityZ(room2,True))<280.0*RoomScale Then
					If Abs(EntityY(mainPlayer\collider)-EntityY(room2,True))<280.0*RoomScale Then
						inside = True
						
						If (Not IsChannelPlaying(event\soundChannels[0])) Then
							event\soundChannels[0] = PlaySound_SM(sndManager\elevatorMove)
						EndIf
						
						mainPlayer\camShake = Sin(Abs(State)/3.0)*0.3
					EndIf
				EndIf
			EndIf	
			
			For n.NPCs = Each NPCs
				If n\CanUseElevator
					If Abs(EntityX(n\collider)-EntityX(room2,True))<280.0*RoomScale
						If Abs(EntityZ(n\collider)-EntityZ(room2,True))<280.0*RoomScale Then
							If Abs(EntityY(n\collider)-EntityY(room2,True))<280.0*RoomScale Then
								NPC_inside = n
							EndIf
						EndIf
					EndIf
				EndIf
			Next
			
			If NPC_inside <> Null And (Not inside)
				NPC_inside\Idle = True
			ElseIf NPC_inside <> Null And inside
				NPC_inside\Idle = False
			EndIf
			
			If State > 400 Then 
				door1\locked = False
				door2\locked = False
				door1\NPCCalledElevator = False
				door2\NPCCalledElevator = False
				State = 0
				
				;pelaaja hissin sis�ll�, siirret��n
				If inside Then	
					x# = Max(Min((EntityX(mainPlayer\collider)-EntityX(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					z# = Max(Min((EntityZ(mainPlayer\collider)-EntityZ(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					PositionEntity(mainPlayer\collider, EntityX(room1,True)+x,0.1+EntityY(room1,True)+(EntityY(mainPlayer\collider)-EntityY(room2,True)),EntityZ(room1,True)+z,True)
					ResetEntity mainPlayer\collider
					UpdateDoorsTimer = 0
					mainPlayer\dropSpeed = 0
					UpdateDoors()
					UpdateRooms()
				EndIf
				
				If NPC_inside <> Null
					x# = Max(Min((EntityX(NPC_inside\collider)-EntityX(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					z# = Max(Min((EntityZ(NPC_inside\collider)-EntityZ(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					PositionEntity(NPC_inside\collider, EntityX(room1,True)+x,0.1+EntityY(room1,True)+(EntityY(NPC_inside\collider)-EntityY(room2,True)),EntityZ(room1,True)+z,True)
					ResetEntity NPC_inside\collider
					UpdateDoorsTimer = 0
					NPC_inside\dropSpeed = 0
					If NPC_inside\Idle
						TurnEntity NPC_inside\obj,0,180,0
						TurnEntity NPC_inside\collider,0,180,0
						NPC_inside\Idle = False
					EndIf
					NPC_inside\CurrElevator = Null
				EndIf
				
				UseDoor(door1,False)
				
				PlayRangedSound_SM(sndManager\elevatorBeep, mainPlayer\cam, room1, 4.0)				
			EndIf	
			
		EndIf
	EndIf
	
	Return State
	
End Function

Function UpdateElevators2#(State#, door1.Doors, door2.Doors, room1, room2, event.Events)
	Local x#, z#
	
	door1\IsElevatorDoor = 1
	door2\IsElevatorDoor = 1
	If door1\open = True And door2\open = False Then 
		State = -1
		door2\IsElevatorDoor = 2
		If (mainPlayer\closestButton = door2\buttons[0] Or mainPlayer\closestButton = door2\buttons[1]) And MouseHit1 Then
			UseDoor(door1,False)
		EndIf
	ElseIf door2\open = True And door1\open = False
		State = 1
		door1\IsElevatorDoor = 2
		If (mainPlayer\closestButton = door1\buttons[0] Or mainPlayer\closestButton = door1\buttons[1]) And MouseHit1 Then
			UseDoor(door2,False)
		EndIf
	ElseIf Abs(door1\openstate-door2\openstate)<0.2 Then
		door1\IsElevatorDoor = 2
		door2\IsElevatorDoor = 2
	EndIf
	
	Local inside = False
	
	;molemmat ovet kiinni = hissi liikkuu
	If door1\open = False And door2\open = False Then
		door1\locked = True 
		door2\locked = True 
		If State < 0 Then ;ylh��lt?alas
			State = State - timing\tickDuration
			;pelaaja hissin sis�ll?
			If Abs(EntityX(mainPlayer\collider)-EntityX(room1,True))<280.0*RoomScale Then
				If Abs(EntityZ(mainPlayer\collider)-EntityZ(room1,True))<280.0*RoomScale Then	
					If Abs(EntityY(mainPlayer\collider)-EntityY(room1,True))<280.0*RoomScale Then	
						inside = True
						
						If (Not IsChannelPlaying(event\soundChannels[0])) Then
							event\soundChannels[0] = PlaySound_SM(sndManager\elevatorMove)
						EndIf
						
						mainPlayer\camShake = Sin(Abs(State)/3.0)*0.3
					EndIf
				EndIf
			EndIf
			
			If State < -400 Then
				door1\locked = False
				door2\locked = False				
				State = 0
				
				UseDoor(door2,True)							
				
				If inside Then
					
					dist# = Distance(EntityX(mainPlayer\collider,True),EntityZ(mainPlayer\collider,True),EntityX(room1,True),EntityZ(room1,True))
					
					dir# = GetAngle(EntityX(mainPlayer\collider,True),EntityZ(mainPlayer\collider,True),EntityX(room1,True),EntityZ(room1,True))
					dir=dir+EntityYaw(room2,True)-EntityYaw(room1,True);EntityYaw(room2,True)+angleDist(dir,EntityYaw(room1,True))
					;dir=dir-90.0
					
;					dir# = EntityYaw(mainPlayer\collider)-EntityYaw(room1,True)+EntityYaw(room2,True)
					
					dir=WrapAngle(dir)
					
					x# = Max(Min(Cos(dir)*dist,280*RoomScale-0.17),-280*RoomScale+0.17)
					z# = Max(Min(Sin(dir)*dist,280*RoomScale-0.17),-280*RoomScale+0.17)
					
					;x# = Max(Min((EntityX(mainPlayer\collider)-EntityX(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					;z# = Max(Min((EntityZ(mainPlayer\collider)-EntityZ(room1,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					
					RotateEntity mainPlayer\collider,EntityPitch(mainPlayer\collider,True),EntityYaw(room2,True)+angleDist(EntityYaw(mainPlayer\collider,True),EntityYaw(room1,True)),EntityRoll(mainPlayer\collider,True),True ;dir
					
					PositionEntity mainPlayer\collider, EntityX(room2,True)+x,0.05+EntityY(room2,True)+(EntityY(mainPlayer\collider)-EntityY(room1,True)),EntityZ(room2,True)+z,True
					
					ResetEntity mainPlayer\collider	
					UpdateDoors()
					UpdateRooms()
				EndIf
				
				PlayRangedSound_SM(sndManager\elevatorBeep, mainPlayer\cam, room2, 4.0)
				;PlaySound(ElevatorBeepSFX)	
			EndIf
		Else ;alhaalta yl�s
			State = State + timing\tickDuration
			;pelaaja hissin sis�ll?
			If Abs(EntityX(mainPlayer\collider)-EntityX(room2,True))<280.0*RoomScale Then
				If Abs(EntityZ(mainPlayer\collider)-EntityZ(room2,True))<280.0*RoomScale Then	
					If Abs(EntityY(mainPlayer\collider)-EntityY(room2,True))<280.0*RoomScale Then
						inside = True
						
						If (Not IsChannelPlaying(event\soundChannels[0])) Then
							event\soundChannels[0] = PlaySound_SM(sndManager\elevatorMove)
						EndIf
						
						mainPlayer\camShake = Sin(Abs(State)/3.0)*0.3
					EndIf
				EndIf
			EndIf	
			
			If State > 400 Then 
				door1\locked = False
				door2\locked = False				
				State = 0
				
				UseDoor(door1,True)	
				
				;pelaaja hissin sis�ll? siirret��n
				If inside Then	
					
					dist# = Distance(EntityX(mainPlayer\collider,True),EntityZ(mainPlayer\collider,True),EntityX(room2,True),EntityZ(room2,True))
					
					dir# = GetAngle(EntityX(mainPlayer\collider,True),EntityZ(mainPlayer\collider,True),EntityX(room2,True),EntityZ(room2,True))
					dir=dir+EntityYaw(room1,True)-EntityYaw(room2,True) ;EntityYaw(room1,True)+angleDist(dir,EntityYaw(room2,True))
					;dir=dir-90.0
					
;					dir# = EntityYaw(mainPlayer\collider)-EntityYaw(room2,True)+EntityYaw(room1,True)
					
					;dir=WrapAngle(dir)
					
					x# = Max(Min(Cos(dir)*dist,280*RoomScale-0.17),-280*RoomScale+0.17)
					z# = Max(Min(Sin(dir)*dist,280*RoomScale-0.17),-280*RoomScale+0.17)
					
					;x# = Max(Min((EntityX(mainPlayer\collider)-EntityX(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					;z# = Max(Min((EntityZ(mainPlayer\collider)-EntityZ(room2,True)),280*RoomScale-0.17),-280*RoomScale+0.17)
					
					RotateEntity mainPlayer\collider,EntityPitch(mainPlayer\collider,True),EntityYaw(room2,True)+angleDist(EntityYaw(mainPlayer\collider,True),EntityYaw(room1,True)),EntityRoll(mainPlayer\collider,True),True ;dir
					
					PositionEntity mainPlayer\collider, EntityX(room1,True)+x,0.05+EntityY(room1,True)+(EntityY(mainPlayer\collider)-EntityY(room2,True)),EntityZ(room1,True)+z,True
					
					ResetEntity mainPlayer\collider
					UpdateDoors()
					UpdateRooms()
				EndIf
				
				PlayRangedSound_SM(sndManager\elevatorBeep, mainPlayer\cam, room1, 4.0)			
			EndIf	
			
		EndIf
	EndIf
	
	Return State
	
End Function
;-------------------------------------------------------------------------------------------------------

Type Props
	Field file$
	Field obj%
	
	Field x#,y#,z#
	Field pitch#,yaw#,roll#
	Field xScale#,yScale#,zScale#
End Type

Function LoadProp.Props(file$,x#,y#,z#,pitch#,yaw#,roll#,xScale#,yScale#,zScale#)
	Local p.Props
	p.Props = New Props
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
	For p2.Props = Each Props
		If (p<>p2) And (p2\file = file) Then
			p\obj = CopyEntity(p2\obj)
			Exit
		EndIf
	Next
	
	If p\obj=0 Then p\obj = LoadMesh("GFX/map/Props/"+file+".b3d")
	If p\obj=0 Then RuntimeError file
	HideEntity(p\obj)
	Return p
End Function

;-------------------------------------------------------------------------------------------------------

Global MapRooms.IntArray = Null ;TODO: replace with an array of the proper type after moving to C++

Function CreateMap()
	DebugLog ("Generating a map using the seed "+RandomSeed)
	
	SeedRnd SeedStringToInt(RandomSeed)
	
	Local mapDim% = MAP_SIZE
	Local layout.IntArray = CreateIntArray(mapDim,mapDim)
	MapRooms = CreateIntArray(mapDim,mapDim)
	
	;clear the grid
	For y% = 0 To mapDim-1
		For x% = 0 To mapDim-1
			SetIntArrayElem(layout,0,x,y)
			SetIntArrayElem(MapRooms,0,x,y)
		Next
	Next
	
	;4x4 squares, offset 1 slot from 0,0
	Local rectWidth% = 3
	Local rectHeight% = 3
	For y% = 0 To mapDim-1
		For x% = 0 To mapDim-1
			If (x Mod rectWidth=1) Or (y Mod rectHeight=1) Then
				If (x>=rectWidth And x<mapDim-rectWidth) Or (y>=rectHeight And y<mapDim-rectHeight) Then
					SetIntArrayElem(layout,1,x,y)
				EndIf
			EndIf
		Next
	Next
	
	DetermineRoomTypes(layout,mapDim)
	
	;shift some horizontal corridors
	Local shift%
	Local nonShiftStreak% = Rand(0,5)
	For y% = 1 To mapDim-2
		For x% = 0 To mapDim-2
			If y>6 Or x>6 Then
				If (y Mod rectHeight=1) And GetIntArrayElem(layout,x,y)=ROOM2 Then
					shift = Rand(0,1)
					If nonShiftStreak=0 Then shift = 0
					If nonShiftStreak>5 Then shift = 1
					If (x/rectWidth) Mod 2 Then shift = -shift
					If shift<>0 Then
						For i% = 0 To rectWidth-2
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
	For y% = 2 To mapDim-4
		For x% = 0 To mapDim-1
			If (((x/rectWidth) Mod 2)=punchOffset) And (GetIntArrayElem(layout,x,y)=ROOM2) Then
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
	For rt.RoomTemplates = Each RoomTemplates
		If ((rt\zones And zone)<>0) And (rt\MaxAmount>0) And (rt\Shape<>ROOM0) Then
			prioritizedTemplateCount=prioritizedTemplateCount+1
		EndIf
	Next
	Local prioritizedTemplates.IntArray = CreateIntArray(prioritizedTemplateCount,1) ;TODO: replace with an array of the right type once we move to C++
	Local tempTemplate.RoomTemplates
	Local tempTemplate2.RoomTemplates
	SetIntArrayElem(prioritizedTemplates,0,0,0)
	For rt.RoomTemplates = Each RoomTemplates
		If ((rt\zones And zone)<>0) And (rt\MaxAmount>0) And (rt\Shape<>ROOM0) Then
			tempTemplate = rt
			DebugLog "queueing up "+rt\Name
			For i%=0 To prioritizedTemplateCount-1
				If GetIntArrayElem(prioritizedTemplates,i,0)=0 Then
					If i<prioritizedTemplateCount-1 Then
						SetIntArrayElem(prioritizedTemplates,0,i+1,0)
					EndIf
					SetIntArrayElem(prioritizedTemplates,Handle(tempTemplate),i,0)
					Exit
				Else
					tempTemplate2 = Object.RoomTemplates(GetIntArrayElem(prioritizedTemplates,i,0))
					If tempTemplate2\MaxAmount>tempTemplate\MaxAmount Then
						SetIntArrayElem(prioritizedTemplates,Handle(tempTemplate),i,0)
						;DebugLog "swapping "+tempTemplate2\Name+" for "+tempTemplate\Name
						tempTemplate = tempTemplate2
					EndIf
				EndIf
			Next
		EndIf
	Next
	
	Local RoomCount%[ROOM4+1]
	For y% = 0 To mapDim-1
		For x% = 0 To mapDim-1
			If GetIntArrayElem(layout,x,y)<>ROOM0 Then RoomCount[GetIntArrayElem(layout,x,y)]=RoomCount[GetIntArrayElem(layout,x,y)]+1
		Next
	Next
	For i% = 1 To ROOM4
		DebugLog "Type"+i+" count: "+RoomCount[i]
	Next
	
	Local r.Rooms
	
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
	For k%=0 To prioritizedTemplateCount-1
		rt.RoomTemplates = Object.RoomTemplates(GetIntArrayElem(prioritizedTemplates,k,0))
		
		placementCount = Rand(rt\MinAmount,rt\MaxAmount)
		
		DebugLog "trying to place "+placementCount+" "+rt\Name
		For c% = 1 To placementCount
			loopStartX = Int(Min(Floor(Float(mapDim)*rt\xRangeStart),mapDim-1))
			loopStartY = Int(Min(Floor(Float(mapDim)*rt\yRangeStart),mapDim-1))
			loopEndX = Int(Min(Floor(Float(mapDim)*rt\xRangeEnd),mapDim-1))
			loopEndY = Int(Min(Floor(Float(mapDim)*rt\yRangeEnd),mapDim-1))
			
			loopX = loopEndX-loopStartX
			loopY = loopEndY-loopStartY
			
			offsetX = Rand(0,loopX)
			offsetY = Rand(0,loopY)
			
			placed = False
			For j% = 0 To loopY
				For i% = 0 To loopX
					x% = ((i+offsetX) Mod (loopX+1)) + loopStartX
					y% = ((j+offsetY) Mod (loopY+1)) + loopStartY
					
					If (GetIntArrayElem(layout,x,y)>0) And (GetIntArrayElem(layout,x,y)=rt\Shape) Then
						r = CreateRoom(rt,x*8.0,0.0,y*8.0)
						r\angle = DetermineRotation(layout,x,y)
						TurnEntity r\obj,0,r\angle,0
						SetIntArrayElem(layout,-1,x,y) ;mark as used
						SetIntArrayElem(MapRooms,Handle(r),x,y) ;add to the MapRooms array
						placed = True
					EndIf
					
					If placed Then Exit
				Next
				If placed Then Exit
			Next
			If Not placed Then RuntimeError "(seed: "+RandomSeed+") Failed To place "+rt\Name+" around ("+loopStartX+","+loopStartY+","+loopEndX+","+loopEndY+")"
		Next
	Next
	
	DeleteIntArray(prioritizedTemplates)
	
	Local randomTemplateCount%
	Local totalCommonness%[ROOM4+1]
	For i% = 1 To ROOM4
		totalCommonness[i] = 0
	Next
	For rt.RoomTemplates = Each RoomTemplates
		If ((rt\zones And zone)<>0) And (rt\MaxAmount<0) And (rt\Shape<>ROOM0) Then
			randomTemplateCount=randomTemplateCount+1
			totalCommonness[rt\Shape]=totalCommonness[rt\Shape]+rt\Commonness
		EndIf
	Next
	Local randomTemplates.IntArray = CreateIntArray(randomTemplateCount,1)
	Local index% = 0
	Local tempHandle1%
	Local tempHandle2%
	
	For rt.RoomTemplates = Each RoomTemplates
		If ((rt\zones And zone)<>0) And (rt\MaxAmount<0) And (rt\Shape<>ROOM0) Then
			SetIntArrayElem(randomTemplates,Handle(rt),index,0)
			index=index+1
		EndIf
	Next
	
	;shuffle the templates
	For i% = 0 To randomTemplateCount-1
		index = Rand(0,randomTemplateCount-1)
		tempHandle1 = GetIntArrayElem(randomTemplates,i,0)
		tempHandle2 = GetIntArrayElem(randomTemplates,index,0)
		SetIntArrayElem(randomTemplates,tempHandle2,i,0)
		SetIntArrayElem(randomTemplates,tempHandle1,index,0)
	Next
	
	Local targetCommonness% = 0
	Local commonnessAccumulator% = 0
	Local currType%
	For y% = 0 To mapDim-1
		For x% = 0 To mapDim-1
			commonnessAccumulator = 0
			currType = GetIntArrayElem(layout,x,y)
			If (currType>0) Then
				targetCommonness = Rand(0,totalCommonness[currType])
				
				For i% = 0 To randomTemplateCount-1
					tempTemplate = Object.RoomTemplates(GetIntArrayElem(randomTemplates,i,0))
					If tempTemplate\shape = currType Then
						commonnessAccumulator=commonnessAccumulator+tempTemplate\Commonness
						If commonnessAccumulator>=targetCommonness Then
							r = CreateRoom(tempTemplate,x*8.0,0.0,y*8.0)
							r\angle = DetermineRotation(layout,x,y)
							TurnEntity r\obj,0,r\angle,0
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
	Local tempWaypoint.WayPoints
	Local newWaypoint.WayPoints
	Local roomAWaypoint.WayPoints
	Local roomBWaypoint.WayPoints
	For y% = 0 To mapDim-1
		For x% = 0 To mapDim-1
			r = Object.Rooms(GetIntArrayElem(MapRooms,x,y))
			If r<>Null Then
				For i% = 0 To 3
					Select i
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
						r\Adjacent[i] = Object.Rooms(GetIntArrayElem(MapRooms,x+tempX,y+tempY))
						If r\Adjacent[i]<>Null Then
							If (r\Adjacent[i]\AdjDoor[(i+2) Mod 4]=Null) Then
								r\AdjDoor[i] = CreateDoor(zone,r\x+4.0*tempX,0.0,r\z+4.0*tempY,90.0*((i+1) Mod 2),Null)
								newWaypoint = CreateWaypoint(r\x+4.0*tempX,50.0*RoomScale,r\z+4.0*tempY,r)
								
								DebugLog "step1"
								roomAWaypoint = Null : roomBWaypoint = Null
								For tempWaypoint = Each WayPoints
									If tempWaypoint<>newWaypoint Then
										If tempWaypoint\room = r Then
											If roomAWaypoint = Null Then
												roomAWaypoint = tempWaypoint
											ElseIf EntityDistance(roomAWaypoint\obj,newWaypoint\obj)>EntityDistance(tempWaypoint\obj,newWaypoint\obj) Then
												roomAWaypoint = tempWaypoint
											EndIf
										EndIf
										
										If tempWaypoint\room = r\Adjacent[i] Then
											If roomBWaypoint = Null Then
												roomBWaypoint = tempWaypoint
											ElseIf EntityDistance(roomBWaypoint\obj,newWaypoint\obj)>EntityDistance(tempWaypoint\obj,newWaypoint\obj) Then
												roomBWaypoint = tempWaypoint
											EndIf
										EndIf
									EndIf
								Next
								
								DebugLog "step2"
								If roomAWaypoint<>Null And roomBWaypoint<>Null Then
									For j% = 0 To 15
										If roomAWaypoint\connected[j]=Null Then
											roomAWaypoint\connected[j]=newWaypoint
											Exit
										EndIf
									Next
									
									For j% = 0 To 15
										If roomBWaypoint\connected[j]=Null Then
											roomBWaypoint\connected[j]=newWaypoint
											Exit
										EndIf
									Next
									
									For j% = 0 To 15
										If newWaypoint\connected[j]=Null Then
											newWaypoint\connected[j]=roomAWaypoint
											newWaypoint\connected[j+1]=roomBWaypoint
											Exit
										EndIf
									Next
								EndIf
								
								DebugLog "step3"
							Else
								r\AdjDoor[i] = r\Adjacent[i]\AdjDoor[(i+2) Mod 4]
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
	For y% = 0 To mapDim-1
		For x% = 0 To mapDim-1
			If GetIntArrayElem(layout,x,y)<>0 Then
				horNeighborCount = 0
				If x>0 Then
					horNeighborCount=horNeighborCount+(GetIntArrayElem(layout,x-1,y)<>0)
				EndIf
				If x<mapDim-1 Then
					horNeighborCount=horNeighborCount+(GetIntArrayElem(layout,x+1,y)<>0)
				EndIf
				vertNeighborCount = 0
				If y>0 Then
					vertNeighborCount=vertNeighborCount+(GetIntArrayElem(layout,x,y-1)<>0)
				EndIf
				If y<mapDim-1 Then
					vertNeighborCount=vertNeighborCount+(GetIntArrayElem(layout,x,y+1)<>0)
				EndIf
				
				If horNeighborCount+vertNeighborCount = 1 Then
					SetIntArrayElem(layout,ROOM1,x,y)
				ElseIf horNeighborCount+vertNeighborCount = 3 Then
					SetIntArrayElem(layout,ROOM3,x,y)
				ElseIf horNeighborCount+vertNeighborCount = 4 Then
					SetIntArrayElem(layout,ROOM4,x,y)
				ElseIf (horNeighborCount = 1) And (vertNeighborCount = 1) Then
					SetIntArrayElem(layout,ROOM2C,x,y)
				ElseIf (horNeighborCount = 2) Xor (vertNeighborCount = 2) Then
					SetIntArrayElem(layout,ROOM2,x,y)
				Else
					SetIntArrayElem(layout,0,x,y)
				EndIf
			EndIf
		Next
	Next
End Function

Function DetermineRotation%(layout.IntArray,x%,y%)
	Select GetIntArrayElem(layout,x,y)
		Case ROOM1
			If (x>0) And (GetIntArrayElem(layout,x-1,y)<>0) Then
				Return 270
			ElseIf (x<layout\xDim-1) And (GetIntArrayElem(layout,x+1,y)<>0) Then
				Return 90
			ElseIf (y>0) And (GetIntArrayElem(layout,x,y-1)<>0) Then
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
			ElseIf (y>0) And (GetIntArrayElem(layout,x,y-1)=0) Then
				Return 180
			ElseIf (x<layout\xDim-1) And (GetIntArrayElem(layout,x+1,y)=0) Then
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

Function CheckRoomOverlap(roomname$, x%, y%)
	Return False ;TODO: reimplement?
End Function

Function GetZone(y%)
	Return -1;TODO: reimplement
End Function

;-------------------------------------------------------------------------------------------------------


Function load_terrain(hmap,yscale#=0.7,t1%,t2%,mask%)
	
	DebugLog "load_terrain: "+hmap
	
	; load the heightmap
	If hmap = 0 Then RuntimeError "Heightmap image "+hmap+" does not exist."
	
	; store heightmap dimensions
	Local x = ImageWidth(hmap)-1, y = ImageHeight(hmap)-1
	Local lx,ly,index
	
	; load texture and lightmaps
	If t1 = 0 Then RuntimeError "load_terrain error: invalid texture 1"
	If t2 = 0 Then RuntimeError "load_terrain error: invalid texture 2"
	If mask = 0 Then RuntimeError "load_terrain error: invalid texture mask"
	
	; auto scale the textures to the right size
	If t1 Then ScaleTexture t1,x/4,y/4
	If t2 Then ScaleTexture t2,x/4,y/4
	If mask Then ScaleTexture mask,x,y
	
	; start building the terrain
	Local mesh = CreateMesh()
	Local surf = CreateSurface(mesh)
	
	; create some verts for the terrain
	For ly = 0 To y
		For lx = 0 To x
			AddVertex surf,lx,0,ly,1.0/lx,1.0/ly
		Next
	Next
	RenderWorld
			
	; connect the verts with faces
	For ly = 0 To y-1
		For lx = 0 To x-1
			AddTriangle surf,lx+((x+1)*ly),lx+((x+1)*ly)+(x+1),(lx+1)+((x+1)*ly)
			AddTriangle surf,(lx+1)+((x+1)*ly),lx+((x+1)*ly)+(x+1),(lx+1)+((x+1)*ly)+(x+1)
		Next
	Next
			
	; position the terrain to center 0,0,0
	Local mesh2% = CopyMesh(mesh,mesh)
	Local surf2% = GetSurface(mesh2,1)
	PositionMesh mesh, -x/2.0,0,-y/2.0
	PositionMesh mesh2, -x/2.0,0.01,-y/2.0
	
	; alter vertice height to match the heightmap red channel
	LockBuffer ImageBuffer(hmap)
	LockBuffer TextureBuffer(mask)
	;SetBuffer 
	For lx = 0 To x
		For ly = 0 To y
			;using vertex alpha and two meshes instead of FE_ALPHAWHATEVER
			;it doesn't look perfect but it does the job
			;you might get better results by downscaling the mask to the same size as the heightmap
			Local maskX# = Min(lx*Float(TextureWidth(mask))/Float(ImageWidth(hmap)),TextureWidth(mask)-1)
			Local maskY# = TextureHeight(mask)-Min(ly*Float(TextureHeight(mask))/Float(ImageHeight(hmap)),TextureHeight(mask)-1)
			RGB1=ReadPixelFast(Min(lx,x-1),y-Min(ly,y-1),ImageBuffer(hmap))
			r=(RGB1 And $FF0000)Shr 16 ;separate out the red
			Local alpha#=(((ReadPixelFast(Max(maskX-5,5),Max(maskY-5,5),TextureBuffer(mask)) And $FF000000) Shr 24)/$FF)
			alpha#=alpha+(((ReadPixelFast(Min(maskX+5,TextureWidth(mask)-5),Min(maskY+5,TextureHeight(mask)-5),TextureBuffer(mask)) And $FF000000) Shr 24)/$FF)
			alpha#=alpha+(((ReadPixelFast(Max(maskX-5,5),Min(maskY+5,TextureHeight(mask)-5),TextureBuffer(mask)) And $FF000000) Shr 24)/$FF)
			alpha#=alpha+(((ReadPixelFast(Min(maskX+5,TextureWidth(mask)-5),Max(maskY-5,5),TextureBuffer(mask)) And $FF000000) Shr 24)/$FF)
			alpha#=alpha*0.25
			alpha#=Sqr(alpha)
			
			index = lx + ((x+1)*ly)
			VertexCoords surf, index , VertexX(surf,index), r*yscale,VertexZ(surf,index)
			VertexCoords surf2, index , VertexX(surf2,index), r*yscale,VertexZ(surf2,index)
			VertexColor surf2, index, 255.0,255.0,255.0,alpha
			; set the terrain texture coordinates
			VertexTexCoords surf,index,lx,-ly 
			VertexTexCoords surf2,index,lx,-ly 
		Next
	Next
	UnlockBuffer TextureBuffer(mask)
	UnlockBuffer ImageBuffer(hmap)
	
	UpdateNormals mesh
	UpdateNormals mesh2
	
	EntityTexture mesh,t1,0,0
	;EntityTexture mesh,mask,0,1
	EntityTexture mesh2,t2,0,0;2
	
	EntityFX mesh, 1
	EntityFX mesh2, 1+2+32
	
	Return mesh
End Function

;TODO: Move to checkpoint event.
Function UpdateCheckpointMonitors(numb%)
	Local i,sf,b,t1
	Local entity%
	
	If numb% = 0
		entity% = Monitor2
		;UpdateCheckpoint1 = True
	Else
		entity% = Monitor3
		;UpdateCheckpoint2 = True
	EndIf
	
	For i = 2 To CountSurfaces(entity)
		sf = GetSurface(entity,i)
		b = GetSurfaceBrush(sf)
		If b<>0 Then
			t1 = GetBrushTexture(b,0)
			If t1<>0 Then
				name$ = StripPath(TextureName(t1))
				If Lower(name) <> "monitortexture.jpg"
					If numb% = 0
						If MonitorTimer# < 50
							BrushTexture b, MonitorTexture2, 0, 0
						Else
							BrushTexture b, MonitorTexture4, 0, 0
						EndIf
					Else
						If MonitorTimer2# < 50
							BrushTexture b, MonitorTexture2, 0, 0
						Else
							BrushTexture b, MonitorTexture3, 0, 0
						EndIf
					EndIf
					PaintSurface sf,b
				EndIf
				If name<>"" Then FreeTexture t1
			EndIf
			FreeBrush b
		EndIf
	Next
	
End Function

Function TurnCheckpointMonitorsOff(numb%)
	Local i,sf,b,t1
	Local entity%
	
	If numb% = 0
		entity% = Monitor2
		;UpdateCheckpoint1 = False
		MonitorTimer# = 0.0
	Else
		entity% = Monitor3
		;UpdateCheckpoint2 = False
		MonitorTimer2# = 0.0
	EndIf
	
	For i = 2 To CountSurfaces(entity)
		sf = GetSurface(entity,i)
		b = GetSurfaceBrush(sf)
		If b<>0 Then
			t1 = GetBrushTexture(b,0)
			If t1<>0 Then
				name$ = StripPath(TextureName(t1))
				If Lower(name) <> "monitortexture.jpg"
					BrushTexture b, MonitorTextureOff, 0, 0
					PaintSurface sf,b
				EndIf
				If name<>"" Then FreeTexture t1
			EndIf
			FreeBrush b
		EndIf
	Next
	
End Function

Function TimeCheckpointMonitors()
	
;	If UpdateCheckpoint1
;		If MonitorTimer < 100.0
;			MonitorTimer# = Min(MonitorTimer# + timing\tickDuration,100.0)
;		Else
;			MonitorTimer# = 0.0
;		EndIf
;	EndIf
;	If UpdateCheckpoint2
;		If MonitorTimer2 < 100.0
;			MonitorTimer2# = Min(MonitorTimer2# + timing\tickDuration,100.0)
;		Else
;			MonitorTimer2# = 0.0
;		EndIf
;	EndIf
	
End Function

Function AmbientLightRooms(value%=0)
	Local mesh%,surf%,brush%,tex0%
	
	If value=AmbientLightRoomVal Then Return
	AmbientLightRoomVal = value
	
	Local oldbuffer% = BackBuffer() ;probably shouldn't make assumptions here but who cares, why wouldn't it use the backbuffer ;GetBuffer()
	
	SetBuffer TextureBuffer(AmbientLightRoomTex)
	
	ClsColor value,value,value
	Cls
	ClsColor 0,0,0
	
	SetBuffer oldbuffer
End Function

;TODO: clean up
Type ChunkPart
	Field Amount%
	Field obj%[128]
	Field RandomYaw#[128]
	Field ID
End Type

Function CreateChunkParts(r.Rooms)
	Local File$ = "Data\1499chunks.INI"
	Local ChunkAmount% = GetINIInt(File$,"general","count")
	Local i%,StrTemp$,j%
	Local chp.ChunkPart,chp2.ChunkPart
	Local obj%
	
	SeedRnd SeedStringToInt(RandomSeed)
	
	For i = 0 To ChunkAmount%
		Local loc% = GetINISectionLocation(File$,"chunk"+i)
		If loc > 0 Then
			StrTemp$ = GetINIString2(File,loc%,"count")
			chp = New ChunkPart
			chp\Amount% = Int(StrTemp$)
			DebugLog "------------------"
			For j = 0 To Int(StrTemp$)
				Local objID% = GetINIString2(File$,loc%,"obj"+j)
				Local x$ = GetINIString2(File$,loc%,"obj"+j+"-x")
				Local z$ = GetINIString2(File$,loc%,"obj"+j+"-z")
				Local yaw$ = GetINIString2(File$,loc%,"obj"+j+"-yaw")
				DebugLog "1499 chunk X/Z/Yaw: "+x$+"|"+z$+"|"+yaw$
				chp\obj%[j] = CopyEntity(r\Objects[objID%])
				If Lower(yaw$) = "random"
					chp\RandomYaw#[j] = Rnd(360)
					RotateEntity chp\obj[j],0,chp\RandomYaw[j],0
				Else
					RotateEntity chp\obj[j],0,Float(yaw),0
				EndIf
				PositionEntity chp\obj[j],Float(x),0,Float(z)
				ScaleEntity chp\obj[j],RoomScale,RoomScale,RoomScale
				EntityType chp\obj[j],HIT_MAP
				EntityPickMode chp\obj[j],2
				;EntityParent chp\obj[j],r\obj
			Next
			chp2 = Before(chp)
			If chp2 <> Null
				chp\ID = chp2\ID+1
			EndIf
			DebugLog "<<<<<<<<<<<<<<<<"
			DebugLog "Generated 1499 chunk "+chp\ID+" sucessfully"
		EndIf
	Next
	
	SeedRnd MilliSecs()
	
End Function

Type Chunk
	Field obj%[128]
	Field x#,z#,y#
	Field Amount%
	;Field debugobj%
End Type

Function CreateChunk.Chunk(obj%,x#,y#,z#,spawnNPCs%=True)
	Local ch.Chunk = New Chunk
	Local chp.ChunkPart,i,n.NPCs
	
	;If obj%<>0
	;	ch\obj% = CopyEntity(obj%)
	;	PositionEntity ch\obj%,x,y,z
	;	ScaleEntity ch\obj%,RoomScale,RoomScale,RoomScale
	;	EntityType ch\obj%,HIT_MAP
	;EndIf
	
	;ch\debugobj% = CreateCube()
	;ScaleEntity ch\debugobj%,20,20,20
	;PositionEntity ch\debugobj%,x#,y#+20,z#
	;EntityColor ch\debugobj%,Rand(0,255),Rand(0,255),Rand(0,255)
	;EntityFX ch\debugobj%,1+FE_WIRE
	
	If obj% > -1
		For chp = Each ChunkPart
			If chp\ID = obj%
				ch\Amount% = chp\Amount%
				For i = 0 To chp\Amount
					ch\obj[i] = CopyEntity(chp\obj[i])
					PositionEntity ch\obj[i],x#,y#,z#
					;ScaleEntity ch\obj[i],RoomScale,RoomScale,RoomScale
					MoveEntity ch\obj[i],EntityX(chp\obj[i]),0,EntityZ(chp\obj[i])
				Next
				Exit
			EndIf
		Next
		If spawnNPCs%
		For i = 0 To Rand(5,10)
			n.NPCs = CreateNPC(NPCtype1499,x+Rnd(-60.0,60.0),y+0.5,z+Rnd(-60.0,60.0))
			If Rand(2)=1 Then n\State2 = 500*3
			n\Angle = Rnd(360)
		Next
	EndIf
	EndIf
	
	ch\x = x
	ch\z = z
	ch\y = y
	
	Return ch
End Function

Function UpdateChunks(r.Rooms,ChunkPartAmount%,spawnNPCs%=True)
	Local ch.Chunk, ch2.Chunk, chp.ChunkPart, ChunkPartAmount2%
	Local ChunkHideDistance% = 120
	Local temp% = False, temp2% = False
	Local x#,z#,i%,y#,CurrChunkX#,CurrChunkZ#
	Local obj%
	
	For ch = Each Chunk
		;If Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),ch\x,ch\z)<ChunkHideDistance
		;	;If ch\obj <> 0 Then ShowEntity ch\obj
		;	If ch\obj[0]<>0
		;		For i = 0 To ch\Amount
		;			ShowEntity ch\obj[i]
		;		Next
		;	EndIf
		;Else
		;	;If ch\obj <> 0 Then HideEntity ch\obj
		;	If ch\obj[0]<>0
		;		For i = 0 To ch\Amount
		;			HideEntity ch\obj[i]
		;		Next
		;	EndIf
		;EndIf
		If ch\obj[0]<>0
			For i = 0 To ch\Amount
				ShowEntity ch\obj[i]
			Next
		EndIf
		y# = ch\y
		If Abs(EntityX(mainPlayer\collider)-ch\x)<20
			If Abs(EntityZ(mainPlayer\collider)-ch\z)<20
				CurrChunkX# = ch\x
				CurrChunkZ# = ch\z
			EndIf
		EndIf
	Next
	
	;CurrChunkX# = Int(EntityX(mainPlayer\collider)/40)*40
	;CurrChunkZ# = Int(EntityZ(mainPlayer\collider)/40)*40
	
	x# = -(ChunkHideDistance+(CurrChunkX#))
	z# = -(ChunkHideDistance+(CurrChunkZ#))
	
	Local StrTemp$ = ""
	SeedRnd SeedStringToInt(RandomSeed)
	
	Repeat
		temp2% = False
		For ch = Each Chunk
			If (ch\x=x#) And (ch\z=z#)
				temp2% = True
				Exit
			EndIf
		Next
		If (Not temp2%)
			;ch2 = CreateChunk(r\Objects[Rand(1,ChunkPartAmount%)],x#,y#,z#)
			ChunkPartAmount2 = GetINIInt("Data\1499chunks.INI","general","count")
			ch2 = CreateChunk(Rand(0,ChunkPartAmount2),x#,y#,z#,spawnNPCs%)
		EndIf
		If x# < (ChunkHideDistance+(CurrChunkX#))
			x# = x# + 40
		Else
			If z# < (ChunkHideDistance+(CurrChunkZ#))
				x# = -(ChunkHideDistance+(CurrChunkX#))
				z# = z# + 40
			Else
				Exit
			EndIf
		EndIf
	Forever
	
	SeedRnd MilliSecs()
	
End Function

Function HideChunks()
	Local ch.Chunk,i
	
	For ch = Each Chunk
		;If ch\obj <> 0 Then HideEntity ch\obj
		If ch\obj[0]<>0
			For i = 0 To ch\Amount
				HideEntity ch\obj[i]
			Next
		EndIf
	Next
	
End Function

Function DeleteChunks()
	
	Delete Each Chunk
	Delete Each ChunkPart
	
End Function

Type ElevatorObj
	Field obj%
	Field InFacility%
	Field door.Doors
End Type

Function AssignElevatorObj.ElevatorObj(obj%,door.Doors,in_facility%)
	Local eo.ElevatorObj = New ElevatorObj
	
	eo\obj% = obj%
	eo\door = door
	eo\InFacility% = in_facility%
	
	Return eo
End Function

Function DeleteElevatorObjects()
	
	Delete Each ElevatorObj
	
End Function

Function FindAndDeleteFakeMonitor(r.Rooms,x#,y#,z#,Amount%)
	Local i%
	
	For i = 0 To Amount%
		If r\Objects[i]<>0
			If EntityX(r\Objects[i],True) = x#
				If EntityY(r\Objects[i],True) = y#
					If EntityZ(r\Objects[i],True) = z#
						FreeEntity r\Objects[i]
						r\Objects[i]=0
						DebugLog "Deleted Fake Monitor: "+i
						Exit
					EndIf
				EndIf
			EndIf
		EndIf
	Next
	
End Function
;~IDEal Editor Parameters:
;~F#4F#76#9E#A6#BE#CE#DF#1A7#1E2#1EA#1FF#20A#214#315#8B4
;~C#Blitz3D