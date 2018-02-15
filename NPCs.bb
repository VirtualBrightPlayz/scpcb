;[Block]

;[End Block]
Global Curr173.NPCs, Curr106.NPCs, Curr096.NPCs
Const NPCtype173% = 1, NPCtype106% = 2, NPCtypeGuard% = 3, NPCtypeD% = 4
Const NPCtype372% = 6, NPCtypeApache% = 7, NPCtypeMTF% = 8, NPCtype096 = 9
Const NPCtype049% = 10, NPCtypeZombie% = 11, NPCtype5131% = 12, NPCtypeTentacle% = 13
Const NPCtype860% = 14, NPCtype939% = 15, NPCtype066% = 16, NPCtype178% = 17, NPCtypePdPlane% = 18
Const NPCtype966% = 19, NPCtype1048a = 20, NPCtype1499% = 21

Type NPCs
	Field obj%, obj2%, obj3%, obj4%, Collider%
	Field NPCtype%, ID%
	Field DropSpeed#, Gravity%
	Field State#, State2#, State3#, PrevState%
	Field MakingNoise%
	
	Field Frame#
	
	Field Angle#
	Field Sound%, SoundChn%, SoundTimer#
	Field Sound2%, SoundChn2%
	
	Field Speed#, CurrSpeed#
	
	Field texture$
	
	Field Idle#
	
	Field Reload#
	
	Field LastSeen%, LastDist#
	
	Field PrevX#, PrevY#, PrevZ#
	
	Field Target.NPCs, TargetID%
	Field EnemyX#, EnemyY#, EnemyZ#
	
	Field Path.WayPoints[20], PathStatus%, PathTimer#, PathLocation%
	
	Field NVX#,NVY#,NVZ#,NVName$
	
	Field GravityMult# = 1.0
	Field MaxGravity# = 0.2
	
	Field MTFVariant%
	Field MTFLeader.NPCs
	Field IsDead%
	Field BlinkTimer# = 1.0
	Field IgnorePlayer%
	
	Field ManipulateBone%
	Field BoneToManipulate$
	Field BoneToManipulate2$
	Field ManipulationType%
	Field BoneX#,BoneY#,BoneZ#
	Field BonePitch#,BoneYaw#,BoneRoll#
	Field NPCNameInSection$
	Field InFacility% = True
	Field CanUseElevator% = False
	Field CurrElevator.ElevatorObj
	Field HP%
	Field PathX#,PathZ#
	Field Model$
	Field ModelScaleX#,ModelScaleY#,ModelScaleZ#
End Type

Include "NPCs/NPCtype173.bb"
Include "NPCs/NPCtype106.bb"
Include "NPCs/NPCtypeGuard.bb"
Include "NPCs/NPCtypeMTF.bb"
Include "NPCs/NPCtypeD.bb"
Include "NPCs/NPCtype372.bb"
Include "NPCs/NPCtype5131.bb"
Include "NPCs/NPCtype096.bb"
Include "NPCs/NPCtype049.bb"
Include "NPCs/NPCtypeZombie.bb"
Include "NPCs/NPCtypeApache.bb"
Include "NPCs/NPCtypeTentacle.bb"
Include "NPCs/NPCtype860.bb"
Include "NPCs/NPCtype939.bb"
Include "NPCs/NPCtype066.bb"
Include "NPCs/NPCtype178.bb"
Include "NPCs/NPCtype966.bb"
Include "NPCs/NPCtype1048a.bb"
Include "NPCs/NPCtype1499.bb"

Function CreateNPC.NPCs(NPCtype%, x#, y#, z#)
	Local n.NPCs = New NPCs, n2.NPCs
	Local temp#, i%, diff1, bump1, spec1
	Local sf, b, t1
	
	n\NPCtype = NPCtype
	n\GravityMult = 1.0
	n\MaxGravity = 0.2
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
		Case NPCtype372
			InitializeNPCtype372(n)
		Case NPCtype5131
			InitializeNPCtype5131(n)
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
		Case NPCtype178
			InitializeNPCtype178(n)
		Case NPCtype966
			InitializeNPCtype966(n)
		Case NPCtype1048a
			InitializeNPCtype1048a(n)
		Case NPCtype1499
			InitializeNPCtype1499(n)
	End Select
	
	PositionEntity(n\Collider, x, y, z, True)
	PositionEntity(n\obj, x, y, z, True)
	
	ResetEntity(n\Collider)
	
	n\ID = 0
	n\ID = FindFreeNPCID()
	
	DebugLog ("Created NPC "+n\NVName+" (ID: "+n\ID+")")
	
	NPCSpeedChange(n)
	
	Return n
End Function

Function RemoveNPC(n.NPCs)
	
	If n=Null Then Return
	
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
	
	If (n\SoundChn <> 0 And ChannelPlaying(n\SoundChn)) Then
		StopChannel(n\SoundChn)
	EndIf
	
	If n\SoundChn2 <> 0 And ChannelPlaying(n\SoundChn2) Then
		StopChannel(n\SoundChn2)
	EndIf
	
	If n\Sound<>0 Then FreeSound_Strict n\Sound
	If n\Sound2<>0 Then FreeSound_Strict n\Sound2
	
	FreeEntity(n\obj) : n\obj = 0
	FreeEntity(n\Collider) : n\Collider = 0	
	
	Delete n
End Function


Function UpdateNPCs()
	Local n.NPCs, n2.NPCs, d.Doors, de.Decals, r.Rooms, eo.ElevatorObj, eo2.ElevatorObj
	Local i%, dist#, dist2#, angle#, x#, y#, z#, prevFrame#, PlayerSeeAble%, RN$
	
	Local target
	
	For n.NPCs = Each NPCs
		;A variable to determine if the NPC is in the facility or not
		;TODO: remove because this is practically useless
		n\InFacility = CheckForNPCInFacility(n)
		
		Select n\NPCtype
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
			Case NPCtype5131
				UpdateNPCtype5131(n)
			Case NPCtype372
				UpdateNPCtype372(n)
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
			Case NPCtype178
				UpdateNPCtype178(n)
			Case NPCtype966
				UpdateNPCtype966(n)
			Case NPCtype1048a
				UpdateNPCtype1048a(n)
			Case NPCtype1499
				UpdateNPCtype1499(n)
		End Select
		
		If n\IsDead
			EntityType n\Collider,HIT_DEAD
		EndIf
		
		Local gravityDist = Distance(EntityX(Collider),EntityZ(Collider),EntityX(n\Collider),EntityZ(n\Collider))
		
		If gravityDist<HideDistance*0.7 Or n\NPCtype = NPCtype1499 Then
			If n\InFacility = InFacility
				TranslateEntity n\Collider, 0, n\DropSpeed, 0
				
				Local CollidedFloor% = False
				For i% = 1 To CountCollisions(n\Collider)
					If CollisionY(n\Collider, i) < EntityY(n\Collider) - 0.01 Then CollidedFloor = True : Exit
				Next
				
				If CollidedFloor = True Then
					n\DropSpeed# = 0
				Else
					n\DropSpeed# = Max(n\DropSpeed - 0.005*FPSfactor*n\GravityMult,-n\MaxGravity)
				EndIf
			Else
				n\DropSpeed = 0
			EndIf
		Else
			n\DropSpeed = 0
		EndIf
	Next
	
End Function

Function TeleportCloser(n.NPCs)
	Local closestDist# = 0
	Local closestWaypoint.WayPoints
	Local w.WayPoints
	
	Local xtemp#, ztemp#
	
	For w.WayPoints = Each WayPoints
		If w\door = Null Then
			xtemp = Abs(EntityX(w\obj,True)-EntityX(n\Collider,True))
			If xtemp < 10.0 And xtemp > 1.0 Then 
				ztemp = Abs(EntityZ(w\obj,True)-EntityZ(n\Collider,True))
				If ztemp < 10.0 And ztemp > 1.0 Then
					If (EntityDistance(Collider, w\obj)>8) Then
						If (SelectedDifficulty\aggressiveNPCs)Then
							;teleports to the nearby waypoint that takes it closest to the player
							Local newDist# = EntityDistance(Collider, w\obj)
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
		EndIf
	Next
	
	If (closestWaypoint<>Null) Then
		PositionEntity n\Collider, EntityX(closestWaypoint\obj,True), EntityY(closestWaypoint\obj,True)+0.15, EntityZ(closestWaypoint\obj,True), True
		ResetEntity n\Collider
	EndIf
	
End Function

Function OtherNPCSeesMeNPC%(me.NPCs,other.NPCs)
	If other\BlinkTimer<=0.0 Then Return False
	
	If EntityDistance(other\Collider,me\Collider)<6.0 Then
		If Abs(DeltaYaw(other\Collider,me\Collider))<60.0 Then
			Return True
		EndIf
	EndIf
	Return False
End Function

Function MeNPCSeesPlayer%(me.NPCs,disablesoundoncrouch%=False)
	;Return values:
		;False (=0): Player is not detected anyhow
		;True (=1): Player is detected by vision
		;2: Player is detected by emitting a sound
		;3: Player is detected by a camera (only for MTF Units!)
		;4: Player is detected through glass
	
	If NoTarget Then Return False
	
	If (Not PlayerDetected) Or me\NPCtype <> NPCtypeMTF
		If me\BlinkTimer<=0.0 Then Return False
		If EntityDistance(Collider,me\Collider)>(8.0-CrouchState+PlayerSoundVolume) Then Return False
		
		;spots the player if he's either in view or making a loud sound
		If PlayerSoundVolume>1.0
			If (Abs(DeltaYaw(me\Collider,Collider))>60.0) And EntityVisible(me\Collider,Collider)
				Return 1
			ElseIf (Not EntityVisible(me\Collider,Collider))
				If disablesoundoncrouch% And Crouch%
					Return False
				Else
					Return 2
				EndIf
			EndIf
		Else
			If (Abs(DeltaYaw(me\Collider,Collider))>60.0) Then Return False
		EndIf
		Return EntityVisible(me\Collider,Collider)
	Else
		If EntityDistance(Collider,me\Collider)>(8.0-CrouchState+PlayerSoundVolume) Then Return 3
		If EntityVisible(me\Collider, Camera) Then Return True
		
		;spots the player if he's either in view or making a loud sound
		If PlayerSoundVolume>1.0 Then Return 2
		Return 3
	EndIf
	
End Function

Function TeleportMTFGroup(n.NPCs)
	Local n2.NPCs
	
	If n\MTFLeader <> Null Then Return
	
	TeleportCloser(n)
	
	For n2 = Each NPCs
		If n2\NPCtype = NPCtypeMTF
			If n2\MTFLeader <> Null
				PositionEntity n2\Collider,EntityX(n2\MTFLeader\Collider),EntityY(n2\MTFLeader\Collider)+0.1,EntityZ(n2\MTFLeader\Collider)
			EndIf
		EndIf
	Next
	
	DebugLog "Teleported MTF Group (dist:"+EntityDistance(n\Collider,Collider)+")"
	
End Function

Function Shoot(x#, y#, z#, hitProb# = 1.0, particles% = True, instaKill% = False)
	;muzzle flash
	Local p.Particles = CreateParticle(x,y,z, 1, Rnd(0.08,0.1), 0.0, 5)
	TurnEntity p\obj, 0,0,Rnd(360)
	p\Achange = -0.15
	
	LightVolume = TempLightVolume*1.2
	
	If (Not GodMode) Then 
		
		If instaKill Then Kill() : PlaySound_Strict BullethitSFX : Return
		
		If Rnd(1.0) =< hitProb Then
			TurnEntity Camera, Rnd(-3,3), Rnd(-3,3), 0
			
			Local ShotMessageUpdate$
			If WearingVest>0 Then
				If WearingVest = 1 Then
					Select Rand(8)
						Case 1,2,3,4,5
							BlurTimer = 500
							Stamina = 0
							ShotMessageUpdate = "A bullet penetrated your vest, making you gasp."
							Injuries = Injuries + Rnd(0.1,0.5)
						Case 6
							BlurTimer = 500
							ShotMessageUpdate = "A bullet hit your left leg."
							Injuries = Injuries + Rnd(0.8,1.2)
						Case 7
							BlurTimer = 500
							ShotMessageUpdate = "A bullet hit your right leg."
							Injuries = Injuries + Rnd(0.8,1.2)
						Case 8
							BlurTimer = 500
							Stamina = 0
							ShotMessageUpdate = "A bullet struck your neck, making you gasp."
							Injuries = Injuries + Rnd(1.2,1.6)
					End Select	
				Else
					If Rand(10)=1 Then
						BlurTimer = 500
						Stamina = Stamina - 1
						ShotMessageUpdate = "A bullet hit your chest. The vest absorbed some of the damage."
						Injuries = Injuries + Rnd(0.8,1.1)
					Else
						ShotMessageUpdate = "A bullet hit your chest. The vest absorbed most of the damage."
						Injuries = Injuries + Rnd(0.1,0.5)
					EndIf
				EndIf
				
				If Injuries >= 3
					If Rand(3) = 1 Then Kill()
				EndIf
			Else
				Select Rand(6)
					Case 1
						Kill()
					Case 2
						BlurTimer = 500
						ShotMessageUpdate = "A bullet hit your left leg."
						Injuries = Injuries + Rnd(0.8,1.2)
					Case 3
						BlurTimer = 500
						ShotMessageUpdate = "A bullet hit your right leg."
						Injuries = Injuries + Rnd(0.8,1.2)
					Case 4
						BlurTimer = 500
						ShotMessageUpdate = "A bullet hit your right shoulder."
						Injuries = Injuries + Rnd(0.8,1.2)	
					Case 5
						BlurTimer = 500
						ShotMessageUpdate = "A bullet hit your left shoulder."
						Injuries = Injuries + Rnd(0.8,1.2)	
					Case 6
						BlurTimer = 500
						ShotMessageUpdate = "A bullet hit your right shoulder."
						Injuries = Injuries + Rnd(2.5,4.0)
				End Select
			EndIf
			
			;Only updates the message if it's been more than two seconds.
			If (MsgTimer < 64*4) Then
				Msg = ShotMessageUpdate
				MsgTimer = 70*6
			EndIf

			Injuries = Min(Injuries, 4.0)
			
			;Kill()
			PlaySound_Strict BullethitSFX
		ElseIf particles
			pvt = CreatePivot()
			PositionEntity pvt, EntityX(Collider),(EntityY(Collider)+EntityY(Camera))/2,EntityZ(Collider)
			PointEntity pvt, p\obj
			TurnEntity pvt, 0, 180, 0
			
			EntityPick(pvt, 2.5)
			
			If PickedEntity() <> 0 Then 
				PlaySound2(Gunshot3SFX, Camera, pvt, 0.4, Rnd(0.8,1.0))
				
				If particles Then 
					;dust/smoke particles
					p.Particles = CreateParticle(PickedX(),PickedY(),PickedZ(), 0, 0.03, 0, 80)
					p\speed = 0.001
					p\SizeChange = 0.003
					p\A = 0.8
					p\Achange = -0.01
					RotateEntity p\pvt, EntityPitch(pvt)-180, EntityYaw(pvt),0
					
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
			FreeEntity pvt
			
		EndIf
		
	EndIf
	
End Function

Function PlayMTFSound(sound%, n.NPCs)
	If n <> Null Then
		n\SoundChn = PlaySound2(sound, Camera, n\Collider, 8.0)	
	EndIf
	
	
	If SelectedItem <> Null Then
		If SelectedItem\state2 = 3 And SelectedItem\state > 0 Then 
			Select SelectedItem\itemtemplate\tempname 
				Case "radio","fineradio","18vradio"
					If RadioCHN(3)<> 0 Then StopChannel RadioCHN(3)
					RadioCHN(3) = PlaySound_Strict (sound)
			End Select
		EndIf
	EndIf 
End Function

Function MoveToPocketDimension()
	Local r.Rooms
	
	For r.Rooms = Each Rooms
		If r\RoomTemplate\Name = "pocketdimension" Then
			FallTimer = 0
			UpdateDoors()
			UpdateRooms()
			ShowEntity Collider
			PlaySound_Strict(Use914SFX)
			PlaySound_Strict(OldManSFX(5))
			PositionEntity(Collider, EntityX(r\obj),0.8,EntityZ(r\obj))
			DropSpeed = 0
			ResetEntity Collider
			
			BlinkTimer = -10
			
			Injuries = Injuries+0.5
			
			PlayerRoom = r
			
			Return
		EndIf
	Next		
End Function

Function FindFreeNPCID%()
	Local id% = 1
	While (True)
		Local taken% = False
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
	
	For n2.NPCs = Each NPCs
		If n2 <> n And n2\ID = newID Then
			n2\id = FindFreeNPCID()
		EndIf
	Next
End Function

Function Find860Angle(n.NPCs, fr.Forest)
	TFormPoint(EntityX(Collider),EntityY(Collider),EntityZ(Collider),0,PlayerRoom\obj)
	Local playerx = Floor((TFormedX()*RoomScale+6.0)/12.0)
	Local playerz = Floor((TFormedZ()*RoomScale+6.0)/12.0)
	
	TFormPoint(EntityX(n\Collider),EntityY(n\Collider),EntityZ(n\Collider),0,PlayerRoom\obj)
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
						Return point_direction(x-0.5,z-0.5,x2,z2)+EntityYaw(PlayerRoom\obj)+180
					EndIf
					
				EndIf
			Next
		Next
	Else
		Return point_direction(EntityX(n\Collider),EntityZ(n\Collider),EntityX(Collider),EntityZ(Collider))+180
	EndIf		
End Function

Function Console_SpawnNPC(c_input$,state%=-9999)
	Local n.NPCs
	
	Select c_input$ 
		Case "mtf"
			n.NPCs = CreateNPC(NPCtypeMTF, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
		Case "173","scp173","scp-173"
			n.NPCs = CreateNPC(NPCtype173, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
		Case "106","scp106","scp-106","larry"
			n.NPCs = CreateNPC(NPCtype106, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
		Case "guard"
			n.NPCs = CreateNPC(NPCtypeGuard, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
		Case "096","scp096","scp-096"
			n.NPCs = CreateNPC(NPCtype096, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
			If Curr096 = Null Then Curr096 = n
		Case "049","scp049","scp-049"
			n.NPCs = CreateNPC(NPCtype049, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
			If state%=-9999 Then n\State = 2
		Case "zombie","scp-049-2"
			n.NPCs = CreateNPC(NPCtypeZombie, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
			If state%=-9999 Then n\State = 1
		Case "966", "scp966", "scp-966"
			n.NPCs = CreateNPC(NPCtype966, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
		Case "class-d","classd","d"
			n.NPCs = CreateNPC(NPCtypeD, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
		Case "372","scp372","scp-372"
			n.NPCs = CreateNPC(NPCtype372, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
		Case "apache"
			n.NPCs = CreateNPC(NPCtypeApache, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
		Case "513-1","scp513-1","scp-513-1"
			n.NPCs = CreateNPC(NPCtype5131, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
		Case "tentacle"
			n.NPCs = CreateNPC(NPCtypeTentacle, EntityX(Collider),EntityY(Collider),EntityZ(Collider))
		Case "860-2","scp860-2","scp-860-2"
			n.NPCs = CreateNPC(NPCtype860, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
		Case "939","scp939","scp-939"
			n.NPCs = CreateNPC(NPCtype939, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
			If state%=-9999 Then n\State = 1
		Case "066","scp066","scp-066"
			n.NPCs = CreateNPC(NPCtype066, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
		Case "npc178"
			n.NPCs = CreateNPC(NPCtype178, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
		Case "pdplane"
			n.NPCs = CreateNPC(NPCtypePdPlane, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
		Case "1048-a","scp1048-a","scp-1048-a","scp1048a","scp-1048a"
			n.NPCs = CreateNPC(NPCtype1048a, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
		Case "scp-008-1","008-1","scp008-1"
			n.NPCs = CreateNPC(NPCtype008, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
		Case "scp-1499-1","scp1499-1","1499-1"
			n.NPCs = CreateNPC(NPCtype1499, EntityX(Collider),EntityY(Collider)+0.2,EntityZ(Collider))
		Default 
			CreateConsoleMsg("NPC type not found.")
	End Select
	
	If n <> Null
		If state%<>-9999
			n\State = state%
		EndIf
	EndIf
	
End Function

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
					PointEntity bone%,Camera
					PointEntity pvt%,Camera
					n\BonePitch# = CurveAngle(EntityPitch(pvt%),n\BonePitch#,10.0)
					Select TransformNPCManipulationData(n\NPCNameInSection,n\BoneToManipulate,"yaw")
						Case 0
							n\BoneYaw# = CurveAngle(EntityPitch(bone%),n\BoneYaw#,10.0)
							pitchvalue# = n\BoneYaw#
						Case 1
							n\BoneYaw# = CurveAngle(EntityYaw(bone%),n\BoneYaw#,10.0)
							yawvalue# = n\BoneYaw#
						Case 2
							n\BoneYaw# = CurveAngle(EntityRoll(bone%),n\BoneYaw#,10.0)
							rollvalue# = n\BoneYaw#
					End Select
					Select TransformNPCManipulationData(n\NPCNameInSection,n\BoneToManipulate,"pitch")
						Case 0
							pitchvalue# = n\BonePitch#
						Case 1
							yawvalue# = n\BonePitch#
						Case 2
							rollvalue# = n\BonePitch#
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
					n\BonePitch# = CurveAngle(DeltaPitch(bone2%,Camera),n\BonePitch#,10.0)
					Select TransformNPCManipulationData(n\NPCNameInSection,n\BoneToManipulate,"pitch")
						Case 0
							pitchvalue# = n\BonePitch#
						Case 1
							yawvalue# = n\BonePitch#
						Case 2
							rollvalue# = n\BonePitch#
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
							n\BoneYaw# = CurveAngle(EntityPitch(bone%),n\BoneYaw#,10.0)
							pitchvalue# = -n\BoneYaw#
						Case 1
							n\BoneYaw# = CurveAngle(EntityYaw(bone%),n\BoneYaw#,10.0)
							yawvalue# = -n\BoneYaw#
						Case 2
							n\BoneYaw# = CurveAngle(EntityRoll(bone%),n\BoneYaw#,10.0)
							rollvalue# = -n\BoneYaw#
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
					PointEntity pvt%,Camera
					n\BoneYaw# = CurveAngle(EntityPitch(pvt%),n\BoneYaw#,10.0)
					Select TransformNPCManipulationData(n\NPCNameInSection,n\BoneToManipulate,"yaw")
						Case 0
							pitchvalue# = n\BoneYaw#
						Case 1
							yawvalue# = n\BoneYaw#
						Case 2
							rollvalue# = n\BoneYaw#
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

Function GetNPCManipulationValue$(NPC$,bone$,section$,valuetype%=0)
	;valuetype determines what type of variable should the Output be returned
	;0 - String
	;1 - Int
	;2 - Float
	;3 - Boolean
	
	Local value$ = GetINIString("Data\NPCBones.ini",NPC$,bone$+"_"+section$)
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
	
	Select n\NPCtype
		Case NPCtype173,NPCtype106,NPCtype096,NPCtype049,NPCtype939,NPCtypeMTF
			Select SelectedDifficulty\otherFactors
				Case NORMAL
					n\Speed = n\Speed * 1.1
				Case HARD
					n\Speed = n\Speed * 1.2
			End Select
	End Select
	
End Function

Function PlayerInReachableRoom()
	Local RN$ = PlayerRoom\RoomTemplate\Name$
	Local e.Events, temp
	
	;Player is in these rooms, returning false
	If RN = "pocketdimension" Or RN = "gatea" Or RN = "dimension1499" Or RN = "173"
		Return False
	EndIf
	;Player is at GateB and is at the surface, returning false
	If RN = "exit1" And EntityY(Collider)>1040.0*RoomScale
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

Function CheckForNPCInFacility(n.NPCs)
	;False (=0): NPC is not in facility (mostly meant for "dimension1499")
	;True (=1): NPC is in facility
	;2: NPC is in tunnels (maintenance tunnels/049 tunnels/939 storage room, etc...)
	
	If EntityY(n\Collider)>100.0
		Return False
	EndIf
	If EntityY(n\Collider)< -10.0
		Return 2
	EndIf
	If EntityY(n\Collider)> 7.0 And EntityY(n\Collider)<=100.0
		Return 2
	EndIf
	
	Return True
End Function

Function FindNextElevator(n.NPCs)
	Local eo.ElevatorObj, eo2.ElevatorObj
	
	For eo = Each ElevatorObj
		If eo\InFacility = n\InFacility
			If Abs(EntityY(eo\obj,True)-EntityY(n\Collider))<10.0
				For eo2 = Each ElevatorObj
					If eo2 <> eo
						If eo2\InFacility = n\InFacility
							If Abs(EntityY(eo2\obj,True)-EntityY(n\Collider))<10.0
								If EntityDistance(eo2\obj,n\Collider)<EntityDistance(eo\obj,n\Collider)
									n\PathStatus = FindPath(n, EntityX(eo2\obj,True),EntityY(eo2\obj,True),EntityZ(eo2\obj,True))
									n\CurrElevator = eo2
									DebugLog "eo2 found for "+n\NPCtype
									Exit
								EndIf
							EndIf
						EndIf
					EndIf
				Next
				If n\CurrElevator = Null
					n\PathStatus = FindPath(n, EntityX(eo\obj,True),EntityY(eo\obj,True),EntityZ(eo\obj,True))
					n\CurrElevator = eo
					DebugLog "eo found for "+n\NPCtype
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

Function GoToElevator(n.NPCs)
	Local dist#,inside%
	
	If n\PathStatus <> 1
		PointEntity n\obj,n\CurrElevator\obj
		RotateEntity n\Collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),20.0),0
		
		inside% = False
		If Abs(EntityX(n\Collider)-EntityX(n\CurrElevator\obj,True))<280.0*RoomScale
			If Abs(EntityZ(n\Collider)-EntityZ(n\CurrElevator\obj,True))<280.0*RoomScale Then
				If Abs(EntityY(n\Collider)-EntityY(n\CurrElevator\obj,True))<280.0*RoomScale Then
					inside% = True
				EndIf
			EndIf
		EndIf
		
		dist# = EntityDistance(n\Collider,n\CurrElevator\door\frameobj)
		If n\CurrElevator\door\open
			If (dist# > 0.4 And dist# < 0.7) And inside%
				UseDoor(n\CurrElevator\door,False)
				DebugLog n\NPCtype+" used elevator"
			EndIf
		Else
			If dist# < 0.7
				n\CurrSpeed = 0.0
				If n\CurrElevator\door\NPCCalledElevator=False
					n\CurrElevator\door\NPCCalledElevator = True
					DebugLog n\NPCtype+" called elevator"
				EndIf
			EndIf
		EndIf
	EndIf
	
End Function

Function FinishWalking(n.NPCs,startframe#,endframe#,speed#)
	Local centerframe#
	
	If n<>Null
		centerframe# = (endframe#-startframe#)/2
		If n\Frame >= centerframe#
			AnimateNPC(n,startframe#,endframe#,speed#,False)
		Else
			AnimateNPC(n,endframe#,startframe#,-speed#,False)
		EndIf
	EndIf
	
End Function

Function RotateToDirection(n.NPCs)
	
	HideEntity n\Collider
	EntityPick(n\Collider, 1.0)
	If PickedEntity() <> 0 Then
		Local turnToSide% = 0
		TurnEntity n\Collider,0,90,0
		EntityPick(n\Collider,1.0)
		If PickedEntity()=0
			turnToSide% = 1
		EndIf
		TurnEntity n\Collider,0,270,0
		If turnToSide% = 1
			TurnEntity n\Collider,0.0,45,0.0,True
		Else
			TurnEntity n\Collider,0.0,-45,0.0,True
		EndIf
	EndIf
	ShowEntity n\Collider
	
End Function

Function AnimateNPC(n.NPCs, start#, quit#, speed#, loop=True)
	Local newTime#
	
	If speed > 0.0 Then 
		newTime = Max(Min(n\Frame + speed * FPSfactor,quit),start)
		
		If loop And newTime => quit Then
			newTime = start
		EndIf
	Else
		If start < quit Then
			temp% = start
			start = quit
			quit = temp
		EndIf
		
		If loop Then
			newTime = n\Frame + speed * FPSfactor
			
			If newTime < quit Then 
				newTime = start
			Else If newTime > start 
				newTime = quit
			EndIf
		Else
			newTime = Max(Min(n\Frame + speed * FPSfactor,start),quit)
		EndIf
	EndIf
	SetNPCFrame(n, newTime)
	
End Function

Function SetNPCFrame(n.NPCs, frame#)
	If (Abs(n\Frame-frame)<0.001) Then Return
	
	SetAnimTime n\obj, frame
	
	n\Frame = frame
End Function
