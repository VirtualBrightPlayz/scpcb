Include "SourceCode/NPCs/NPCtype173.bb"
Include "SourceCode/NPCs/NPCtype106.bb"
Include "SourceCode/NPCs/NPCtypeGuard.bb"
Include "SourceCode/NPCs/NPCtypeMTF.bb"
Include "SourceCode/NPCs/NPCtypeD.bb"
Include "SourceCode/NPCs/NPCtype096.bb"
Include "SourceCode/NPCs/NPCtype049.bb"
Include "SourceCode/NPCs/NPCtypeZombie.bb"
Include "SourceCode/NPCs/NPCtypeApache.bb"
Include "SourceCode/NPCs/NPCtypeTentacle.bb"
Include "SourceCode/NPCs/NPCtype860.bb"
Include "SourceCode/NPCs/NPCtype939.bb"
Include "SourceCode/NPCs/NPCtype066.bb"
Include "SourceCode/NPCs/NPCtype966.bb"
Include "SourceCode/NPCs/NPCtype1499.bb"

;TODO: replace with functions that return the first instance of each NPC
Global Curr173.NPCs
Global Curr106.NPCs
Global Curr096.NPCs

Const NPCtype173%       = 1
Const NPCtype106%       = 2
Const NPCtype049%       = 3
Const NPCtype096%       = 4
Const NPCtype966%       = 5
Const NPCtype860%       = 6
Const NPCtype939%       = 7
Const NPCtype5131%      = 8
Const NPCtype066%       = 10
Const NPCtypeD%         = 11
Const NPCtypeGuard%     = 12
Const NPCtypeMTF%       = 13
Const NPCtypeApache%    = 14
Const NPCtypeZombie%    = 15 ;TODO: Remove.
Const NPCtypeTentacle%  = 16
Const NPCtype1499%      = 17
Const NPCtypePdPlane%   = 18 ;TODO: Remove, don't think this is ever used even.

Const NPC_SOUND_COUNT% = 12
Const NPC_CHANNEL_COUNT% = 3
Type NPCs
	Field obj%
	Field obj2%
	Field obj3%
	Field obj4%
	Field collider%

	Field npcType%
	Field ID%

	Field polyData%

	;TODO: Deprecate state2 and state3. Make state an integer.
	Field state#
	Field state2#
	Field state3#
	Field prevState%

	;TODO: Deprecate?
	Field makingNoise%
	
	Field frame#
	Field angle#

	Field sounds%[NPC_SOUND_COUNT]
	Field soundChannels%[NPC_CHANNEL_COUNT]
	
	Field playerDistance#
	
	;TODO: wtf why aren't we using this more instead of reload?
	Field soundTimer#
	
	Field speed#, currSpeed#
	
	Field texture$
	
	;TODO: Deprecate in favor of state.
	Field Idle#
	
	;TODO: Deprecate in favor of timer.
	Field Reload#

	Field timer#
	
	Field lastSeen%
	Field lastDist#
	
	Field prevX#
	Field prevY#
	Field prevZ#
	
	Field target.NPCs, targetID%

	;TODO: Deprecate in favor of target.
	Field EnemyX#
	Field EnemyY#
	Field EnemyZ#
	
	Field targetX#
	Field targetY#
	Field targetZ#
	
	Field path.WayPoints[20]
	Field pathStatus%
	Field pathTimer#
	Field pathLocation%
	Field pathX#
	Field pathZ#
	
	Field nvX#
	Field nvY#
	Field nvZ#
	Field nvName$
	
	Field dropSpeed#
	Field gravity%
	Field gravityMult# = 1.0

	;TODO: Deprecate in favor of terminalVelocity.
	Field maxGravity# = 0.2

	Field terminalVelocity# = 0.2
	
	;TODO: Move this to DataMTF
	Field MTFVariant%
	Field MTFLeader.NPCs
	Field IsDead%
	Field BlinkTimer# = 1.0
	Field IgnorePlayer%
	
	;TODO: Deprecate.
	Field NPCNameInSection$
	Field ManipulateBone%
	Field BoneToManipulate$
	Field BoneToManipulate2$
	Field ManipulationType%

	Field boneX#
	Field boneY#
	Field boneZ#

	Field bonePitch#
	Field boneYaw#
	Field boneRoll#

	;TODO: Burn this stuff in hell.
	Field InFacility% = True
	Field CanUseElevator% = False
	Field CurrElevator.ElevatorObj
	Field HP%
	Field Model$
	Field ModelScaleX#,ModelScaleY#,ModelScaleZ#
End Type

Function CreateNPC.NPCs(NPCtype%, x#, y#, z#)
	Local n.NPCs = New NPCs, n2.NPCs
	Local temp#, i%, diff1, bump1, spec1
	Local sf, b, t1
	
	n\npcType = NPCtype
	n\gravityMult = 1.0
	n\maxGravity = 0.2
	Select NPCtype
		Case NPCtype173
			InitializeNPCtype173(n)
		Case NPCtype106
			InitializeNPCtype106(n)
		Case NPCtypeGuard
			InitializeNPCtypeGuard(n)
		Case NPCtypeMTF
			InitializeNPCtypeMTF(n)
		Case NPCtypeD
			InitializeNPCtypeD(n)
		Case NPCtype096
			InitializeNPCtype096(n)
		Case NPCtype049
			InitializeNPCtype049(n)
		Case NPCtypeZombie
			InitializeNPCtypeZombie(n)
		Case NPCtypeApache
			InitializeNPCtypeApache(n)
		Case NPCtypeTentacle
			InitializeNPCtypeTentacle(n)
		Case NPCtype860
			InitializeNPCtype860(n)
		Case NPCtype939
			InitializeNPCtype939(n)
		Case NPCtype066
			InitializeNPCtype066(n)
		Case NPCtype966
			InitializeNPCtype966(n)
		Case NPCtype1499
			InitializeNPCtype1499(n)
	End Select
	
	PositionEntity(n\collider, x, y, z, True)
	PositionEntity(n\obj, x, y, z, True)
	
	ResetEntity(n\collider)
	
	n\ID = 0
	n\ID = FindFreeNPCID()
	
	DebugLog ("Created NPC "+n\nvName+" (ID: "+n\ID+")")
	
	NPCSpeedChange(n)
	
	Return n
End Function

;Attempts to copy an existing mesh from an NPC of the same type.
;Otherwise it loads the mesh from the specified filepath.
Function LoadOrCopyMesh(n.NPCs, filePath$)
	Local n2.NPCs
	For n2.NPCs = Each NPCs
		If (n\npcType = n2\npcType And n <> n2) Then
			If (n2\obj <> 0) Then
				n\obj = CopyEntity(n2\obj)
				Return
			EndIf
		EndIf
	Next
	
	n\obj = LoadAnimMesh(filePath)
End Function

Function RemoveNPC(n.NPCs)
	If (n = Null) Then
		Return
	EndIf
	
	If n\obj2 <> 0 Then 
		FreeEntity n\obj2
		n\obj2 = 0
	EndIf
	If n\obj3 <> 0 Then 
		FreeEntity n\obj3
		n\obj3 = 0
	EndIf
	If n\obj4 <> 0 Then 
		FreeEntity n\obj4
		n\obj4 = 0
	EndIf

	NPCStopAllChannels(n)
	
	Local i%
	For i = 0 To NPC_SOUND_COUNT-1
		If (n\sounds[i] <> 0) Then
			FreeSound(n\sounds[i])
		EndIf
	Next
	
	FreeEntity(n\obj)
	n\obj = 0
	FreeEntity(n\collider)
	n\collider = 0	
	
	Delete(n)
End Function


Function UpdateNPCs()
	Local n.NPCs
	
	For n = Each NPCs
		;A variable to determine if the NPC is in the facility or not
		;TODO: remove because this is practically useless
		n\InFacility = CheckForNPCInFacility(n)
		
		n\playerDistance = EntityDistance(mainPlayer\collider, n\collider)
		
		;If the npc was given a target then use its position.
		If (n\target <> Null) Then
			n\targetX = EntityX(n\target\collider)
			n\targetY = EntityY(n\target\collider)
			n\targetZ = EntityZ(n\target\collider)
		EndIf
		
		;If the player is too far away then don't run the update code.
		If (n\playerDistance >= HideDistance*2) Then
			;TODO: Implement roaming code.
			Return
		EndIf
		
		Select n\npcType
			Case NPCtype173
				UpdateNPCtype173(n)
			Case NPCtype106
				UpdateNPCtype106(n)
			Case NPCtype096
				UpdateNPCtype096(n)
			Case NPCtype049
				UpdateNPCtype049(n)
			Case NPCtypeZombie
				UpdateNPCtypeZombie(n)
			Case NPCtypeGuard
				UpdateNPCtypeGuard(n)
			Case NPCtypeMTF
				UpdateNPCtypeMTF(n)
			Case NPCtypeD
				UpdateNPCtypeD(n)
			Case NPCtypeApache
				UpdateNPCtypeApache(n)
			Case NPCtypeTentacle
				UpdateNPCtypeTentacle(n)
			Case NPCtype860
				UpdateNPCtype860(n) 
			Case NPCtype939
				UpdateNPCtype939(n)
			Case NPCtype066
				UpdateNPCtype066(n)
			Case NPCtype966
				UpdateNPCtype966(n)
			Case NPCtype1499
				UpdateNPCtype1499(n)
		End Select
		
		If n\IsDead
			EntityType(n\collider, HIT_DEAD)
		EndIf
		
		;Update sound locations.
		Local i%
		For i = 0 To 2
			If (IsChannelPlaying(n\soundChannels[i])) Then
				UpdateRangedSoundOrigin(n\soundChannels[i], mainPlayer\cam, n\obj)
			EndIf
		Next

		;TODO: Rework.
		Local gravityDist# = Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider),EntityX(n\collider),EntityZ(n\collider))
		
		If gravityDist<HideDistance*0.7 Or n\npcType = NPCtype1499 Then
			If n\InFacility = InFacility
				TranslateEntity n\collider, 0, n\dropSpeed, 0
				
				Local CollidedFloor% = False
				For i% = 1 To CountCollisions(n\collider)
					If CollisionY(n\collider, i) < EntityY(n\collider) - 0.01 Then CollidedFloor = True : Exit
				Next
				
				If CollidedFloor = True Then
					n\dropSpeed# = 0
				Else
					n\dropSpeed# = Max(n\dropSpeed - 0.005*timing\tickDuration*n\gravityMult,-n\maxGravity)
				EndIf
			Else
				n\dropSpeed = 0
			EndIf
		Else
			n\dropSpeed = 0
		EndIf
	Next
	
End Function

;Stops all audio channels for a given NPC.
Function NPCStopAllChannels(n.NPCs)
	Local i%
	For i = 0 To NPC_CHANNEL_COUNT-1
		If (IsChannelPlaying(n\soundChannels[i])) Then
			StopChannel(n\soundChannels[i])
		EndIf
	Next
End Function

Function TeleportCloser(n.NPCs)
	Local closestDist# = 0
	Local closestWaypoint.WayPoints
	Local w.WayPoints
	
	Local xtemp#, ztemp#
	
	For w.WayPoints = Each WayPoints
		;If w\door = Null Then ;TODO: fix?
		xtemp = Abs(EntityX(w\obj,True)-EntityX(n\collider,True))
		If xtemp < 10.0 And xtemp > 1.0 Then 
			ztemp = Abs(EntityZ(w\obj,True)-EntityZ(n\collider,True))
			If ztemp < 10.0 And ztemp > 1.0 Then
				If (EntityDistance(mainPlayer\collider, w\obj)>8) Then
					If (SelectedDifficulty\aggressiveNPCs)Then
						;teleports to the nearby waypoint that takes it closest to the player
						Local newDist# = EntityDistance(mainPlayer\collider, w\obj)
						If (newDist < closestDist Or closestWaypoint = Null) Then
							closestDist = newDist	
							closestWaypoint = w
						EndIf
					Else 
						;just teleports to the first nearby waypoint it finds
						closestWaypoint = w
						Exit
					EndIf						
				EndIf
			EndIf
		EndIf
		;EndIf
	Next
	
	If (closestWaypoint<>Null) Then
		PositionEntity n\collider, EntityX(closestWaypoint\obj,True), EntityY(closestWaypoint\obj,True)+0.15, EntityZ(closestWaypoint\obj,True), True
		ResetEntity n\collider
	EndIf
	
End Function

;TODO: rename lol
Function OtherNPCSeesMeNPC%(me.NPCs,other.NPCs)
	If other\BlinkTimer<=0.0 Then Return False
	
	If EntityDistance(other\collider,me\collider)<6.0 Then
		If Abs(DeltaYaw(other\collider,me\collider))<60.0 Then
			Return True
		EndIf
	EndIf
	Return False
End Function

Function MeNPCSeesPlayer%(me.NPCs,disableSoundOnCrouch%=False)
	;Return values:
		;False (=0): Player is not detected anyhow
		;True (=1): Player is detected by vision
		;2: Player is detected by emitting a sound
		;3: Player is detected by a camera (only for MTF Units!)
		;4: Player is detected through glass
	
	If NoTarget Then Return False
	
	If (Not PlayerDetected) Or me\npcType <> NPCtypeMTF
		If me\BlinkTimer<=0.0 Then Return False
		If EntityDistance(mainPlayer\collider,me\collider)>(8.0-mainPlayer\crouchState+mainPlayer\loudness) Then Return False
		
		;spots the player if he's either in view or making a loud sound
		If mainPlayer\loudness>1.0
			If (Abs(DeltaYaw(me\collider,mainPlayer\collider))>60.0) And EntityVisible(me\collider,mainPlayer\collider)
				Return 1
			ElseIf (Not EntityVisible(me\collider,mainPlayer\collider))
				If disableSoundOnCrouch% And mainPlayer\crouching
					Return False
				Else
					Return 2
				EndIf
			EndIf
		Else
			If (Abs(DeltaYaw(me\collider,mainPlayer\collider))>60.0) Then Return False
		EndIf
		Return EntityVisible(me\collider,mainPlayer\collider)
	Else
		If EntityDistance(mainPlayer\collider,me\collider)>(8.0-mainPlayer\crouchState+mainPlayer\loudness) Then Return 3
		If EntityVisible(me\collider, mainPlayer\cam) Then Return True
		
		;spots the player if he's either in view or making a loud sound
		If mainPlayer\loudness>1.0 Then Return 2
		Return 3
	EndIf
	
End Function

;TODO: Move to MTF file?
Function TeleportMTFGroup(n.NPCs)
	Local n2.NPCs
	
	If n\MTFLeader <> Null Then Return
	
	TeleportCloser(n)
	
	For n2 = Each NPCs
		If n2\npcType = NPCtypeMTF
			If n2\MTFLeader <> Null
				PositionEntity n2\collider,EntityX(n2\MTFLeader\collider),EntityY(n2\MTFLeader\collider)+0.1,EntityZ(n2\MTFLeader\collider)
			EndIf
		EndIf
	Next
	
	DebugLog "Teleported MTF Group (dist:"+EntityDistance(n\collider,mainPlayer\collider)+")"
	
End Function

Function Shoot(x#, y#, z#, hitProb# = 1.0, particles% = True, instaKill% = False)
	;muzzle flash
	Local p.Particles = CreateParticle(x,y,z, 1, Rnd(0.08,0.1), 0.0, 5)
	TurnEntity p\obj, 0,0,Rnd(360)
	p\Achange = -0.15
	
	;LightVolume = TempLightVolume*1.2
	
	If (Not mainPlayer\godMode) Then 
		
		If instaKill Then
			Kill(mainPlayer)
			PlaySound_SM(sndManager\bulletHit)
			Return
		EndIf
		
		If Rnd(1.0) =< hitProb Then
			TurnEntity mainPlayer\cam, Rnd(-3,3), Rnd(-3,3), 0
			
			Local ShotMessageUpdate$
			Local WearingVest% = False
			WearingVest = WearingVest Or IsPlayerWearingTempName(mainPlayer,"vest")
			WearingVest = WearingVest Or IsPlayerWearingTempName(mainPlayer,"finevest")
			WearingVest = WearingVest Or IsPlayerWearingTempName(mainPlayer,"veryfinevest")
			If WearingVest Then
				If IsPlayerWearingTempName(mainPlayer,"vest") Then
					Select Rand(8)
						Case 1,2,3,4,5
							mainPlayer\blurTimer = 500
							mainPlayer\stamina = 0
							ShotMessageUpdate = "A bullet penetrated your vest, making you gasp."
							mainPlayer\injuries = mainPlayer\injuries + Rnd(0.1,0.5)
						Case 6
							mainPlayer\blurTimer = 500
							ShotMessageUpdate = "A bullet hit your left leg."
							mainPlayer\injuries = mainPlayer\injuries + Rnd(0.8,1.2)
						Case 7
							mainPlayer\blurTimer = 500
							ShotMessageUpdate = "A bullet hit your right leg."
							mainPlayer\injuries = mainPlayer\injuries + Rnd(0.8,1.2)
						Case 8
							mainPlayer\blurTimer = 500
							mainPlayer\stamina = 0
							ShotMessageUpdate = "A bullet struck your neck, making you gasp."
							mainPlayer\injuries = mainPlayer\injuries + Rnd(1.2,1.6)
					End Select	
				Else
					If Rand(10)=1 Then
						mainPlayer\blurTimer = 500
						mainPlayer\stamina = mainPlayer\stamina - 1
						ShotMessageUpdate = "A bullet hit your chest. The vest absorbed some of the damage."
						mainPlayer\injuries = mainPlayer\injuries + Rnd(0.8,1.1)
					Else
						ShotMessageUpdate = "A bullet hit your chest. The vest absorbed most of the damage."
						mainPlayer\injuries = mainPlayer\injuries + Rnd(0.1,0.5)
					EndIf
				EndIf
				
				If mainPlayer\injuries >= 3
					If Rand(3) = 1 Then Kill(mainPlayer)
				EndIf
			Else
				Select Rand(6)
					Case 1
						Kill(mainPlayer)
					Case 2
						mainPlayer\blurTimer = 500
						ShotMessageUpdate = "A bullet hit your left leg."
						mainPlayer\injuries = mainPlayer\injuries + Rnd(0.8,1.2)
					Case 3
						mainPlayer\blurTimer = 500
						ShotMessageUpdate = "A bullet hit your right leg."
						mainPlayer\injuries = mainPlayer\injuries + Rnd(0.8,1.2)
					Case 4
						mainPlayer\blurTimer = 500
						ShotMessageUpdate = "A bullet hit your right shoulder."
						mainPlayer\injuries = mainPlayer\injuries + Rnd(0.8,1.2)	
					Case 5
						mainPlayer\blurTimer = 500
						ShotMessageUpdate = "A bullet hit your left shoulder."
						mainPlayer\injuries = mainPlayer\injuries + Rnd(0.8,1.2)	
					Case 6
						mainPlayer\blurTimer = 500
						ShotMessageUpdate = "A bullet hit your right shoulder."
						mainPlayer\injuries = mainPlayer\injuries + Rnd(2.5,4.0)
				End Select
			EndIf
			
			;Only updates the message if it's been more than two seconds.
			If (MsgTimer < 64*4) Then
				Msg = ShotMessageUpdate
				MsgTimer = 70*6
			EndIf

			mainPlayer\injuries = Min(mainPlayer\injuries, 4.0)
			
			;Kill(mainPlayer)
			PlaySound_SM(sndManager\bulletHit)
		ElseIf particles Then
			Local pvt% = CreatePivot()
			PositionEntity pvt, EntityX(mainPlayer\collider),(EntityY(mainPlayer\collider)+EntityY(mainPlayer\cam))/2,EntityZ(mainPlayer\collider)
			PointEntity pvt, p\obj
			TurnEntity pvt, 0, 180, 0
			
			EntityPick(pvt, 2.5)
			
			If PickedEntity() <> 0 Then 
				PlayRangedSound_SM(sndManager\bulletMiss, mainPlayer\cam, pvt, 0.4, Rnd(0.8,1.0))
				
				If particles Then 
					;dust/smoke particles
					p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.03, 0, 80)
					p\speed = 0.001
					p\SizeChange = 0.003
					p\A = 0.8
					p\Achange = -0.01
					RotateEntity p\pvt, EntityPitch(pvt)-180, EntityYaw(pvt),0
					
					Local i%
					For i = 0 To Rand(2,3)
						p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.006, 0.003, 80)
						p\speed = 0.02
						p\A = 0.8
						p\Achange = -0.01
						RotateEntity p\pvt, EntityPitch(pvt)+Rnd(170,190), EntityYaw(pvt)+Rnd(-10,10),0	
					Next
					
					;bullet hole decal
					Local de.Decals = CreateDecal(Rand(13,14), PickedX(),PickedY(),PickedZ(), 0,0,0)
					AlignToVector de\obj,-PickedNX(),-PickedNY(),-PickedNZ(),3
					MoveEntity de\obj, 0,0,-0.001
					EntityFX de\obj, 1
					de\lifetime = 70*20
					EntityBlend de\obj, 2
					de\Size = Rnd(0.028,0.034)
					ScaleSprite de\obj, de\Size, de\Size
				EndIf				
			EndIf
			FreeEntity(pvt)
			
		EndIf
		
	EndIf
	
End Function

;TODO: Move to MTF file?
Function PlayMTFSound(sound%, n.NPCs)
	If n <> Null Then
		n\soundChannels[0] = PlayRangedSound(sound, mainPlayer\cam, n\collider, 8.0)	
	EndIf
	
	If mainPlayer\selectedItem <> Null Then
		If mainPlayer\selectedItem\state2 = 3 And mainPlayer\selectedItem\state > 0 Then 
			Select mainPlayer\selectedItem\itemtemplate\tempname 
				Case "radio","fineradio","18vradio"
					If RadioCHN(3)<> 0 Then StopChannel RadioCHN(3)
					RadioCHN(3) = PlaySound(sound)
			End Select
		EndIf
	EndIf 
End Function

;TODO: Does this even have anything to do with NPCs? Move to Player file whenever that's made?
Function MoveToPocketDimension()
	Local r.Rooms
	
	For r.Rooms = Each Rooms
		If r\RoomTemplate\Name = "pocketdimension" Then
			mainPlayer\fallTimer = 0
			UpdateDoors()
			UpdateRooms()
			ShowEntity mainPlayer\collider
			PlaySound2(LoadTempSound("SFX/SCP/914/PlayerUse.ogg"))
			;PlaySound2(OldManSFX(5)) ;TODO: fix
			PositionEntity(mainPlayer\collider, EntityX(r\obj),0.8,EntityZ(r\obj))
			mainPlayer\dropSpeed = 0
			ResetEntity mainPlayer\collider
			
			mainPlayer\blinkTimer = -10
			
			mainPlayer\injuries = mainPlayer\injuries+0.5
			
			mainPlayer\currRoom = r
			
			Return
		EndIf
	Next		
End Function

Function FindFreeNPCID%()
	Local id% = 1
	While (True)
		Local taken% = False
		
		Local n2.NPCs
		For n2.NPCs = Each NPCs
			If n2\ID = id Then
				taken = True
				Exit
			EndIf
		Next
		If (Not taken) Then
			Return id
		EndIf
		id = id + 1
	Wend
End Function

Function ForceSetNPCID(n.NPCs, newID%)
	n\ID = newID
	
	Local n2.NPCs
	For n2.NPCs = Each NPCs
		If n2 <> n And n2\ID = newID Then
			n2\ID = FindFreeNPCID()
		EndIf
	Next
End Function

;TODO: Move to 860 creature file.
Function Find860Angle(n.NPCs, fr.Forest)
	TFormPoint(EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider),0,mainPlayer\currRoom\obj)
	Local playerx = Floor((TFormedX()*RoomScale+6.0)/12.0)
	Local playerz = Floor((TFormedZ()*RoomScale+6.0)/12.0)
	
	TFormPoint(EntityX(n\collider),EntityY(n\collider),EntityZ(n\collider),0,mainPlayer\currRoom\obj)
	Local x# = (TFormedX()*RoomScale+6.0)/12.0
	Local z# = (TFormedZ()*RoomScale+6.0)/12.0
	
	Local xt = Floor(x), zt = Floor(z)
	
	Local x2,z2
	If xt<>playerx Or zt<>playerz Then ;the monster is not on the same tile as the player
		For x2 = Max(xt-1,0) To Min(xt+1,gridsize-1)
			For z2 = Max(zt-1,0) To Min(zt+1,gridsize-1)
				If fr\grid[(z2*gridsize)+x2]>0 And (x2<>xt Or z2<>zt) And (x2=xt Or z2=zt) Then
					
					;tile (x2,z2) is closer to the player than the monsters current tile
					If (Abs(playerx-x2)+Abs(playerz-z2))<(Abs(playerx-xt)+Abs(playerz-zt)) Then
						Return GetAngle(x-0.5,z-0.5,x2,z2)+EntityYaw(mainPlayer\currRoom\obj)+180
					EndIf
					
				EndIf
			Next
		Next
	Else
		Return GetAngle(EntityX(n\collider),EntityZ(n\collider),EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider))+180
	EndIf		
End Function

;TODO: Reimplement the revamp of this function from post-1.3.2.
Function Console_SpawnNPC(c_input$,state%=-9999)
	Local n.NPCs
	
	Select c_input$ 
		Case "mtf"
			n.NPCs = CreateNPC(NPCtypeMTF, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
		Case "173","scp173","scp-173"
			n.NPCs = CreateNPC(NPCtype173, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
		Case "106","scp106","scp-106","larry"
			n.NPCs = CreateNPC(NPCtype106, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
		Case "guard"
			n.NPCs = CreateNPC(NPCtypeGuard, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
		Case "096","scp096","scp-096"
			n.NPCs = CreateNPC(NPCtype096, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
			If Curr096 = Null Then Curr096 = n
		Case "049","scp049","scp-049"
			n.NPCs = CreateNPC(NPCtype049, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
			If state%=-9999 Then n\state = 2
		Case "zombie","scp-049-2"
			n.NPCs = CreateNPC(NPCtypeZombie, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
			If state%=-9999 Then n\state = 1
		Case "966", "scp966", "scp-966"
			n.NPCs = CreateNPC(NPCtype966, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
		Case "class-d","classd","d"
			n.NPCs = CreateNPC(NPCtypeD, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
		Case "apache"
			n.NPCs = CreateNPC(NPCtypeApache, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
		Case "513-1","scp513-1","scp-513-1"
			n.NPCs = CreateNPC(NPCtype5131, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
		Case "tentacle"
			n.NPCs = CreateNPC(NPCtypeTentacle, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider))
		Case "860-2","scp860-2","scp-860-2"
			n.NPCs = CreateNPC(NPCtype860, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
		Case "939","scp939","scp-939"
			n.NPCs = CreateNPC(NPCtype939, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
			If state%=-9999 Then n\state = 1
		Case "066","scp066","scp-066"
			n.NPCs = CreateNPC(NPCtype066, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
		Case "pdplane"
			n.NPCs = CreateNPC(NPCtypePdPlane, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
		Case "1048-a","scp1048-a","scp-1048-a","scp1048a","scp-1048a"
			n.NPCs = CreateNPC(NPCtype1048a, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
		Case "scp-008-1","008-1","scp008-1"
			n.NPCs = CreateNPC(NPCtype008, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
		Case "scp-1499-1","scp1499-1","1499-1"
			n.NPCs = CreateNPC(NPCtype1499, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider)+0.2,EntityZ(mainPlayer\collider))
		Default 
			CreateConsoleMsg("NPC type not found.")
	End Select
	
	If n <> Null
		If state%<>-9999
			n\state = state%
		EndIf
	EndIf
	
End Function

;TODO: Restore pre-shitty bone system iteration of this function.
Function ManipulateNPCBones()
	Local n.NPCs,bone%,bone2%,pvt%,pitch#,yaw#,roll#
	Local bonename$,bonename2$
	Local pitchvalue#,yawvalue#,rollvalue#
	Local pitchoffset#,yawoffset#,rolloffset#
	
	For n = Each NPCs
		If n\ManipulateBone
			pitchvalue# = 0
			yawvalue# = 0
			rollvalue# = 0
			pitchoffset# = TransformNPCManipulationData(n\NPCNameInSection,n\BoneToManipulate,"pitchoffset")
			yawoffset# = TransformNPCManipulationData(n\NPCNameInSection,n\BoneToManipulate,"yawoffset")
			rolloffset# = TransformNPCManipulationData(n\NPCNameInSection,n\BoneToManipulate,"rolloffset")
			pvt% = CreatePivot()
			bonename$ = GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"bonename",0)
			bone% = FindChild(n\obj,bonename$)
			If bone% = 0 Then RuntimeError "ERROR: NPC bone "+Chr(34)+bonename$+Chr(34)+" does not exist."
			If n\BoneToManipulate2<>""
				bonename2$ = GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"navbone",0)
				bone2% = FindChild(n\obj,n\BoneToManipulate2$)
				If bone2% = 0 Then RuntimeError "ERROR: NPC bone "+Chr(34)+bonename2$+Chr(34)+" does not exist."
			EndIf
			PositionEntity pvt%,EntityX(bone%,True),EntityY(bone%,True),EntityZ(bone%,True)
			Select n\ManipulationType
				Case 0 ;<--- looking at player
					PointEntity bone%,mainPlayer\cam
					PointEntity pvt%,mainPlayer\cam
					n\bonePitch# = CurveAngle(EntityPitch(pvt%),n\bonePitch#,10.0)
					Select TransformNPCManipulationData(n\NPCNameInSection,n\BoneToManipulate,"yaw")
						Case 0
							n\boneYaw# = CurveAngle(EntityPitch(bone%),n\boneYaw#,10.0)
							pitchvalue# = n\boneYaw#
						Case 1
							n\boneYaw# = CurveAngle(EntityYaw(bone%),n\boneYaw#,10.0)
							yawvalue# = n\boneYaw#
						Case 2
							n\boneYaw# = CurveAngle(EntityRoll(bone%),n\boneYaw#,10.0)
							rollvalue# = n\boneYaw#
					End Select
					Select TransformNPCManipulationData(n\NPCNameInSection,n\BoneToManipulate,"pitch")
						Case 0
							pitchvalue# = n\bonePitch#
						Case 1
							yawvalue# = n\bonePitch#
						Case 2
							rollvalue# = n\bonePitch#
					End Select
					If GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"pitchinverse",3)=True
						pitchvalue# = -pitchvalue#
					EndIf
					If GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"yawinverse",3)=True
						yawvalue# = -yawvalue#
					EndIf
					If GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"rollinverse",3)=True
						rollvalue# = -rollvalue#
					EndIf
					RotateEntity bone%,pitchvalue#+pitchoffset#,yawvalue#+yawoffset#,rollvalue#+rolloffset#
				Case 1 ;<--- looking at player #2
					n\bonePitch# = CurveAngle(DeltaPitch(bone2%,mainPlayer\cam),n\bonePitch#,10.0)
					Select TransformNPCManipulationData(n\NPCNameInSection,n\BoneToManipulate,"pitch")
						Case 0
							pitchvalue# = n\bonePitch#
						Case 1
							yawvalue# = n\bonePitch#
						Case 2
							rollvalue# = n\bonePitch#
					End Select
					If GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"pitchinverse",3)=True
						pitchvalue# = -pitchvalue#
					EndIf
					If GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"yawinverse",3)=True
						yawvalue# = -yawvalue#
					EndIf
					If GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"rollinverse",3)=True
						rollvalue# = -rollvalue#
					EndIf
					RotateEntity bone%,pitchvalue#+pitchoffset#,yawvalue#+yawoffset#,rollvalue#+rolloffset#
				Case 2 ;<--- looking away from SCP-096
					PointEntity bone%,Curr096\obj
					Select TransformNPCManipulationData(n\NPCNameInSection,n\BoneToManipulate,"yaw")
						Case 0
							n\boneYaw# = CurveAngle(EntityPitch(bone%),n\boneYaw#,10.0)
							pitchvalue# = -n\boneYaw#
						Case 1
							n\boneYaw# = CurveAngle(EntityYaw(bone%),n\boneYaw#,10.0)
							yawvalue# = -n\boneYaw#
						Case 2
							n\boneYaw# = CurveAngle(EntityRoll(bone%),n\boneYaw#,10.0)
							rollvalue# = -n\boneYaw#
					End Select
					If GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"pitchinverse",3)=True
						pitchvalue# = -pitchvalue#
					EndIf
					If GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"yawinverse",3)=True
						yawvalue# = -yawvalue#
					EndIf
					If GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"rollinverse",3)=True
						rollvalue# = -rollvalue#
					EndIf
					RotateEntity bone%,pitchvalue#+pitchoffset#,yawvalue#+yawoffset#,rollvalue#+rolloffset#
				Case 3 ;<-- looking and pitching towards the player
					PointEntity pvt%,mainPlayer\cam
					n\boneYaw# = CurveAngle(EntityPitch(pvt%),n\boneYaw#,10.0)
					Select TransformNPCManipulationData(n\NPCNameInSection,n\BoneToManipulate,"yaw")
						Case 0
							pitchvalue# = n\boneYaw#
						Case 1
							yawvalue# = n\boneYaw#
						Case 2
							rollvalue# = n\boneYaw#
					End Select
					If GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"pitchinverse",3)=True
						pitchvalue# = -pitchvalue#
					EndIf
					If GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"yawinverse",3)=True
						yawvalue# = -yawvalue#
					EndIf
					If GetNPCManipulationValue(n\NPCNameInSection,n\BoneToManipulate,"rollinverse",3)=True
						rollvalue# = -rollvalue#
					EndIf
					RotateEntity bone%,pitchvalue#+pitchoffset#,yawvalue#+yawoffset#,rollvalue#+rolloffset#
			End Select
			FreeEntity pvt%
		EndIf
	Next
	
End Function

;TODO: Remove this, go back to being hardcoded.
Function GetNPCManipulationValue$(NPC$,bone$,section$,valuetype%=0)
	;valuetype determines what type of variable should the Output be returned
	;0 - String
	;1 - Int
	;2 - Float
	;3 - Boolean
	
	Local value$ = GetINIString("Data/NPCBones.ini",NPC$,bone$+"_"+section$)
	Select valuetype%
		Case 0
			Return value$
		Case 1
			Return Int(value$)
		Case 2
			Return Float(value$)
		Case 3
			If value$ = "true" Or value$ = "1"
				Return True
			Else
				Return False
			EndIf
	End Select
	
End Function

;TODO: A function that returns an int or a float depending on context, brilliant! Remove as part of restoration of previous bone system.
Function TransformNPCManipulationData(NPC$,bone$,section$)
	;If "section$" = "pitch","yaw" or "roll":
	;	- 0 means "realpitch" value has detected
	;	- 1 means "realyaw" value has detected
	;	- 2 means "realroll" value has detected
	;If "section$" = "pitchoffset","yawoffset","rolloffset":
	;	- simply return the offset degree value using a "return Float"
	
	Local value$ = GetNPCManipulationValue(NPC$,bone$,section$)
	Select section$
		Case "pitch","yaw","roll"
			Select value$
				Case "realpitch"
					Return 0
				Case "realyaw"
					Return 1
				Case "realroll"
					Return 2
			End Select
		Case "pitchoffset","yawoffset","rolloffset"
			Return Float(value$)
	End Select
	
End Function

Function NPCSpeedChange(n.NPCs)
	
	Select n\npcType
		Case NPCtype173,NPCtype106,NPCtype096,NPCtype049,NPCtype939,NPCtypeMTF
			Select SelectedDifficulty\otherFactors
				Case NORMAL
					n\speed = n\speed * 1.1
				Case HARD
					n\speed = n\speed * 1.2
			End Select
	End Select
	
End Function

Function PlayerInReachableRoom()
	Local RN$ = mainPlayer\currRoom\RoomTemplate\Name$
	Local e.Events, temp
	
	;Player is in these rooms, returning false
	If RN = "pocketdimension" Or RN = "gatea" Or RN = "dimension1499" Or RN = "173"
		Return False
	EndIf
	;Player is at GateB and is at the surface, returning false
	If RN = "exit1" And EntityY(mainPlayer\collider)>1040.0*RoomScale
		Return False
	EndIf
	;Player is in 860's test room and inside the forest, returning false
	temp = False
	For e = Each Events
		If e\EventName$ = "room860" And e\EventState = 1.0
			temp = True
			Exit
		EndIf
	Next
	If RN = "room860" And temp
		Return False
	EndIf
	;Return true, this means player is in reachable room
	Return True
	
End Function

;TODO: Remove in favor of doing checks as needed.
Function CheckForNPCInFacility(n.NPCs)
	;False (=0): NPC is not in facility (mostly meant for "dimension1499")
	;True (=1): NPC is in facility
	;2: NPC is in tunnels (maintenance tunnels/049 tunnels/939 storage room, etc...)
	
	If EntityY(n\collider)>100.0
		Return False
	EndIf
	If EntityY(n\collider)< -10.0
		Return 2
	EndIf
	If EntityY(n\collider)> 7.0 And EntityY(n\collider)<=100.0
		Return 2
	EndIf
	
	Return True
End Function

;TODO: Remove?
Function FindNextElevator(n.NPCs)
	Local eo.ElevatorObj, eo2.ElevatorObj
	
	For eo = Each ElevatorObj
		If eo\InFacility = n\InFacility
			If Abs(EntityY(eo\obj,True)-EntityY(n\collider))<10.0
				For eo2 = Each ElevatorObj
					If eo2 <> eo
						If eo2\InFacility = n\InFacility
							If Abs(EntityY(eo2\obj,True)-EntityY(n\collider))<10.0
								If EntityDistance(eo2\obj,n\collider)<EntityDistance(eo\obj,n\collider)
									n\pathStatus = FindPath(n, EntityX(eo2\obj,True),EntityY(eo2\obj,True),EntityZ(eo2\obj,True))
									n\CurrElevator = eo2
									DebugLog "eo2 found for "+n\npcType
									Exit
								EndIf
							EndIf
						EndIf
					EndIf
				Next
				If n\CurrElevator = Null
					n\PathStatus = FindPath(n, EntityX(eo\obj,True),EntityY(eo\obj,True),EntityZ(eo\obj,True))
					n\CurrElevator = eo
					DebugLog "eo found for "+n\npcType
				EndIf
				If n\PathStatus <> 1
					n\CurrElevator = Null
					DebugLog "Unable to find elevator path: Resetting CurrElevator"
				EndIf
				Exit
			EndIf
		EndIf
	Next
	
End Function

;TODO: Remove?
Function GoToElevator(n.NPCs)
	Local dist#,inside%
	
	If n\pathStatus <> 1
		PointEntity n\obj,n\CurrElevator\obj
		RotateEntity n\collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\collider),20.0),0
		
		inside% = False
		If Abs(EntityX(n\collider)-EntityX(n\CurrElevator\obj,True))<280.0*RoomScale
			If Abs(EntityZ(n\collider)-EntityZ(n\CurrElevator\obj,True))<280.0*RoomScale Then
				If Abs(EntityY(n\collider)-EntityY(n\CurrElevator\obj,True))<280.0*RoomScale Then
					inside% = True
				EndIf
			EndIf
		EndIf
		
		dist# = EntityDistance(n\collider,n\CurrElevator\door\frameobj)
		If n\CurrElevator\door\open
			If (dist# > 0.4 And dist# < 0.7) And inside%
				UseDoor(n\CurrElevator\door,False)
				DebugLog n\npcType+" used elevator"
			EndIf
		Else
			If dist# < 0.7
				n\currSpeed = 0.0
				If n\CurrElevator\door\NPCCalledElevator=False
					n\CurrElevator\door\NPCCalledElevator = True
					DebugLog n\npcType+" called elevator"
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

Function FinishWalking(n.NPCs,startframe#,endframe#,speed#)
	Local centerframe#
	
	If n<>Null
		centerframe# = (endframe#-startframe#)/2
		If n\frame >= centerframe#
			AnimateNPC(n,startframe#,endframe#,speed#,False)
		Else
			AnimateNPC(n,endframe#,startframe#,-speed#,False)
		EndIf
	EndIf
	
End Function

Function RotateToDirection(n.NPCs)
	
	HideEntity n\collider
	EntityPick(n\collider, 1.0)
	If PickedEntity() <> 0 Then
		Local turnToSide% = 0
		TurnEntity n\collider,0,90,0
		EntityPick(n\collider,1.0)
		If PickedEntity()=0
			turnToSide% = 1
		EndIf
		TurnEntity n\collider,0,270,0
		If turnToSide% = 1
			TurnEntity n\collider,0.0,45,0.0,True
		Else
			TurnEntity n\collider,0.0,-45,0.0,True
		EndIf
	EndIf
	ShowEntity n\collider
	
End Function

Function AnimateNPC(n.NPCs, start#, quit#, speed#, loop=True)
	Local newTime#
	
	If speed > 0.0 Then 
		newTime = Max(Min(n\frame + speed * timing\tickDuration, quit), start)
		
		If loop And newTime => quit Then
			newTime = start
		EndIf
	Else
		If start < quit Then
			Local temp% = start
			start = quit
			quit = temp
		EndIf
		
		If loop Then
			newTime = n\frame + speed * timing\tickDuration
			
			If newTime < quit Then 
				newTime = start
			Else If newTime > start Then
				newTime = quit
			EndIf
		Else
			newTime = Max(Min(n\frame + speed * timing\tickDuration, start), quit)
		EndIf
	EndIf
	SetNPCFrame(n, newTime)
	
End Function

Function SetNPCFrame(n.NPCs, frame#)
	If (Abs(n\frame-frame)<0.001) Then Return
	
	SetAnimTime n\obj, frame
	
	n\frame = frame
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D