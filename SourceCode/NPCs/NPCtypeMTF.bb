Global MTFtimer#, MTFrooms.Room[10], MTFroomState%[10]

Function InitializeNPCtypeMTF(n.NPC)
    n\nvName = "Human"
    n\collider = CreatePivot()
    EntityRadius(n\collider, 0.2)
    ;EntityRadius(mainPlayer\collider, 0.15, 0.30)
    EntityType(n\collider, HIT_PLAYER)
    ;EntityPickMode(n\collider, 1)

	Local n2.NPC
	For n2 = Each NPC
		If (n2\npcType = n\npcType And n2\obj <> 0) Then
			n\obj = CopyEntity(n2\obj)
			Exit
		EndIf
	Next

	If (n\obj = 0) Then
		n\obj = LoadAnimMesh("GFX/NPCs/MTF/mtf.b3d")
	EndIf

    n\speed = (GetINIFloat("Data/NPCs.ini", "MTF", "speed") / 100.0)

    Local temp# = (GetINIFloat("Data/NPCs.ini", "MTF", "scale") / 2.5)

    ScaleEntity(n\obj, temp, temp, temp)

    MeshCullBox(n\obj, -MeshWidth(n\obj), -MeshHeight(n\obj), -MeshDepth(n\obj), MeshWidth(n\obj)*2, MeshHeight(n\obj)*2, MeshDepth(n\obj)*2)

	;TODO; Re-implement with MTF struct.
    ;If (MTFSFX(0)=0) Then
    ;    MTFSFX(0)=LoadSound("SFX/Character/MTF/ClassD1.ogg")
    ;    MTFSFX(1)=LoadSound("SFX/Character/MTF/ClassD2.ogg")
    ;    MTFSFX(2)=LoadSound("SFX/Character/MTF/ClassD3.ogg")
    ;    MTFSFX(3)=LoadSound("SFX/Character/MTF/ClassD4.ogg")
    ;    MTFSFX(4)=LoadSound("SFX/Character/MTF/Tesla0.ogg")
    ;    MTFSFX(5)=LoadSound("SFX/Character/MTF/Beep.ogg")
    ;    MTFSFX(6)=LoadSound("SFX/Character/MTF/Breath.ogg")
    ;EndIf

	Local r.Room
    If (MTFrooms[6]=Null) Then
        For r = Each Room
            Select Lower(r\roomTemplate\name)
                Case "room106"
                    MTFrooms[0]=r
                Case "roompj"
                    MTFrooms[1]=r
                Case "room079"
                    MTFrooms[2]=r
                Case "room2poffices"
                    MTFrooms[3]=r
                Case "914"
                    MTFrooms[4]=r
                Case "coffin"
                    MTFrooms[5]=r
                Case "start"
                    MTFrooms[6]=r
            End Select
        Next
    EndIf
End Function

Function UpdateNPCtypeMTF(n.NPC)
	Local x#,y#,z#, tmp%
	Local r.Room
	Local prevDist#,newDist#, prev%
	Local n2.NPC

	Local p.Particle, target%, dist#, dist2#, wp.WayPoint
	Local foundChamber%
	Local pvt%, temp%, soundVol173#, angle#, curr173Dist#, tempDist#

	If (n\isDead) Then
		n\blinkTimer = -1.0
		SetNPCFrame(n, 532)
		Return
	EndIf

	n\maxGravity = 0.03

	n\blinkTimer = n\blinkTimer - timing\tickDuration
	If (n\blinkTimer<=-5.0) Then
		;only play the "blinking" sound clip if searching/containing 173
		If (n\state = 2) Then
			If (OtherNPCSeesMeNPC(Curr173,n)) Then
				PlayMTFSound(LoadTempSound("SFX/Character/MTF/173/BLINKING.ogg"),n)
			EndIf
		EndIf
		n\blinkTimer = 70.0*Rnd(10.0,15.0)
	EndIf

	n\reload = n\reload - timing\tickDuration

	Local prevFrame# = n\frame

	n\boneToManipulate = ""
	n\boneToManipulate2 = ""
	n\manipulateBone = False
	n\manipulationType = 0
	n\npcNameInSection = "MTF"

	If (Int(n\state) <> 1) Then n\prevState = 0

	If (n\idle>0.0) Then
		FinishWalking(n,488,522,0.015*26)
		n\idle=n\idle-timing\tickDuration
		If (n\idle<=0.0) Then n\idle = 0.0
	Else
		Select Int(n\state) ;what is this MTF doing
			Case 0 ;wandering around
                ;[Block]
                n\speed = 0.015
                If (n\pathTimer<=0.0) Then ;update path
					If (n\mtfLeader<>Null) Then ;i'll follow the leader
						n\pathStatus = FindPath(n,EntityX(n\mtfLeader\collider,True),EntityY(n\mtfLeader\collider,True)+0.1,EntityZ(n\mtfLeader\collider,True)) ;whatever you say boss
					Else ;i am the leader
						If (Curr173\idle<>2) Then
							For r = Each Room
								If (((Abs(r\x-EntityX(n\collider,True))>12.0) Or (Abs(r\z-EntityZ(n\collider,True))>12.0)) And (Rand(1,Int(Max(4-Int(Abs(r\z-EntityZ(n\collider,True)/8.0)),2)))=1)) Then
									x = r\x
									y = 0.1
									z = r\z
									DebugLog(r\roomTemplate\name)
									Exit
								EndIf
							Next
						Else
							tmp = False
							If (EntityDistance(n\collider,Curr173\collider)>4.0) Then
								If (Not EntityVisible(n\collider,Curr173\collider)) Then
									tmp = True
								EndIf
							EndIf

							If (Not tmp) Then
								For r = Each Room
									If (r\roomTemplate\name$ = "start") Then
										foundChamber = False
										pvt = CreatePivot()
										PositionEntity(pvt,EntityX(r\obj,True)+4736*RoomScale,0.5,EntityZ(r\obj,True)+1692*RoomScale)

										If (Distance(EntityX(pvt),EntityZ(pvt),EntityX(n\collider),EntityZ(n\collider))<3.5) Then
											foundChamber = True
											DebugLog(Str(Distance(EntityX(pvt),EntityZ(pvt),EntityX(n\collider),EntityZ(n\collider))))
										EndIf

										If (Curr173\idle = 3 And Distance(EntityX(pvt),EntityZ(pvt),EntityX(n\collider),EntityZ(n\collider)) > 4.0) Then
											If (r\doors[1]\open = True) Then UseDoor(r\doors[1],False)
										EndIf

										FreeEntity(pvt)

										If (Distance(EntityX(n\collider),EntityZ(n\collider),EntityX(r\obj,True)+4736*RoomScale,EntityZ(r\obj,True)+1692*RoomScale)>1.6 And (Not foundChamber)) Then
											x = EntityX(r\obj,True)+4736*RoomScale
											y = 0.1
											z = EntityZ(r\obj,True)+1692*RoomScale
											DebugLog("Move to 173's chamber")
											Exit
										ElseIf (Distance(EntityX(n\collider),EntityZ(n\collider),EntityX(r\obj,True)+4736*RoomScale,EntityZ(r\obj,True)+1692*RoomScale)>1.6 And foundChamber) Then
											n\pathX = EntityX(r\obj,True)+4736*RoomScale
											n\pathZ = EntityZ(r\obj,True)+1692*RoomScale
											DebugLog("Move inside 173's chamber")
											Exit
										Else
											Curr173\idle = 3
											Curr173\target = Null
											Curr173\isDead = True
											If (n\sounds[0] <> 0) Then
												FreeSound(n\sounds[0])
												n\sounds[0] = 0
											EndIf
											n\sounds[0] = LoadSound("SFX/Character/MTF/173/Cont"+Str(Rand(1,4))+".ogg")
											PlayMTFSound(n\sounds[0], n)
											DebugLog("173 contained")
											Exit
										EndIf
									EndIf
								Next
							Else
								x = EntityX(Curr173\collider)
								y = 0.1
								z = EntityZ(Curr173\collider)
								DebugLog("Going back to 173's cage")
							EndIf
						EndIf
						If (n\pathX=0) Then n\pathStatus = FindPath(n,x,y,z) ;we're going to this room for no particular reason
					EndIf
					If (n\pathStatus = 1) Then
						While n\path[n\pathLocation]=Null
							If (n\pathLocation>19) Then Exit
							n\pathLocation=n\pathLocation+1
						Wend
						If (n\pathLocation<19) Then
							If (n\path[n\pathLocation]<>Null) And (n\path[n\pathLocation+1]<>Null) Then
								;If (n\path[n\pathLocation]\door=Null) Then ;TODO: fix?
								If (Abs(DeltaYaw(n\collider,n\path[n\pathLocation]\obj))>Abs(DeltaYaw(n\collider,n\path[n\pathLocation+1]\obj))) Then
									n\pathLocation=n\pathLocation+1
								EndIf
								;EndIf
							EndIf
						EndIf
					EndIf
					n\pathTimer = 70.0 * Rnd(6.0,10.0) ;search again after 6-10 seconds
                ElseIf (n\pathTimer<=70.0 * 2.5) And (n\mtfLeader=Null) Then
					n\pathTimer=n\pathTimer-timing\tickDuration
					n\currSpeed = 0.0
					If (Rand(1,35)=1) Then
						RotateEntity(n\collider,0.0,Rnd(360.0),0.0,True)
					EndIf
					FinishWalking(n,488,522,n\speed*26)
					n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
					RotateEntity(n\obj,-90.0,n\angle,0.0,True)
                Else
					If (n\pathStatus=2) Then
						n\pathTimer=n\pathTimer-(timing\tickDuration*2.0) ;timer goes down fast
						n\currSpeed = 0.0
						If (Rand(1,35)=1) Then
							RotateEntity(n\collider,0.0,Rnd(360.0),0.0,True)
						EndIf
						FinishWalking(n,488,522,n\speed*26)
						n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
						RotateEntity(n\obj,-90.0,n\angle,0.0,True)
					ElseIf (n\pathStatus=1) Then
						If (n\path[n\pathLocation]=Null) Then
							If (n\pathLocation > 19) Then
								n\pathLocation = 0 : n\pathStatus = 0
							Else
								n\pathLocation = n\pathLocation + 1
							EndIf
						Else
							prevDist = EntityDistance(n\collider,n\path[n\pathLocation]\obj)

							PointEntity(n\collider,n\path[n\pathLocation]\obj)
							RotateEntity(n\collider,0.0,EntityYaw(n\collider,True),0.0,True)

							RotateToDirection(n)

							n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)

							RotateEntity(n\obj,-90.0,n\angle,0.0,True)

							n\currSpeed = CurveValue(n\speed,n\currSpeed,20.0)
							;MoveEntity(n\collider, 0, 0, n\currSpeed * timing\tickDuration)

							TranslateEntity(n\collider, Cos(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, 0, Sin(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, True)
							AnimateNPC(n,488, 522, n\currSpeed*26)

							newDist = EntityDistance(n\collider,n\path[n\pathLocation]\obj)

							;TODO: fix
							;If (newDist<1.0 And n\path[n\pathLocation]\door<>Null) Then
							;	;open the door and make it automatically close after 5 seconds
							;	If (Not n\path[n\pathLocation]\door\open)
							;		PlayMTFSound(MTFSFX(5),n)
							;	EndIf
							;	n\path[n\pathLocation]\door\open = True
							;	If n\path[n\pathLocation]\door\mtfClose
							;		n\path[n\pathLocation]\door\timerstate = 70.0*5.0
							;	EndIf
							;EndIf

							If (newDist<0.2) Or ((prevDist<newDist) And (prevDist<1.0)) Then
								n\pathLocation=n\pathLocation+1
							EndIf
						EndIf
						n\pathTimer=n\pathTimer-timing\tickDuration ;timer goes down slow
					ElseIf (n\pathX#<>0.0) Then
						pvt = CreatePivot()
						PositionEntity(pvt,n\pathX#,0.5,n\pathZ#)

						PointEntity(n\collider,pvt)
						RotateEntity(n\collider,0.0,EntityYaw(n\collider,True),0.0,True)
						n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
						RotateEntity(n\obj,-90.0,n\angle,0.0,True)

						n\currSpeed = CurveValue(n\speed,n\currSpeed,20.0)
						TranslateEntity(n\collider, Cos(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, 0, Sin(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, True)
						AnimateNPC(n,488, 522, n\currSpeed*26)

						If (Distance(EntityX(n\collider),EntityZ(n\collider),n\pathX#,n\pathZ#)<0.2) Then
							n\pathX# = 0.0
							n\pathZ# = 0.0
							n\pathTimer = 70.0 * Rnd(6.0,10.0)
						EndIf

						FreeEntity(pvt)
					Else
						n\pathTimer=n\pathTimer-(timing\tickDuration*2.0) ;timer goes down fast
						If (n\mtfLeader = Null) Then
							If (Rand(1,35)=1) Then
								RotateEntity(n\collider,0.0,Rnd(360.0),0.0,True)
							EndIf
							FinishWalking(n,488,522,n\speed*26)
							n\currSpeed = 0.0
						ElseIf (EntityDistance(n\collider,n\mtfLeader\collider)>1.0) Then
							PointEntity(n\collider,n\mtfLeader\collider)
							RotateEntity(n\collider,0.0,EntityYaw(n\collider,True),0.0,True)

							n\currSpeed = CurveValue(n\speed,n\currSpeed,20.0)
							TranslateEntity(n\collider, Cos(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, 0, Sin(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, True)
							AnimateNPC(n,488, 522, n\currSpeed*26)
						Else
							If (Rand(1,35)=1) Then
								RotateEntity(n\collider,0.0,Rnd(360.0),0.0,True)
							EndIf
							FinishWalking(n,488,522,n\speed*26)
							n\currSpeed = 0.0
						EndIf
						n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
						RotateEntity(n\obj,-90.0,n\angle,0.0,True)
					EndIf
                EndIf

				temp = MeNPCSeesPlayer(n)

				If (NoTarget) Then temp = False

                If (temp>False) Then
					If (n\lastSeen > 0 And n\lastSeen < 70*15) Then
						If (temp < 2) Then
							If (n\sounds[0] <> 0) Then
								FreeSound(n\sounds[0])
								n\sounds[0] = 0
							EndIf
							n\sounds[0] = LoadSound("SFX/Character/MTF/ThereHeIs"+Str(Rand(1,6))+".ogg")
							PlayMTFSound(n\sounds[0], n)
						EndIf
					Else
						If (temp = True) Then
							If (n\sounds[0] <> 0) Then
								FreeSound(n\sounds[0])
								n\sounds[0] = 0
							EndIf
							n\sounds[0] = LoadSound("SFX/Character/MTF/Stop"+Str(Rand(1,6))+".ogg")
							PlayMTFSound(n\sounds[0], n)
						ElseIf (temp = 2) Then
							;If (n\sounds[0] <> 0) Then FreeSound(n\sounds[0] : n\sounds[0] = 0)
							;n\sounds[0] = MTFSFX(Rand(0,3))
							;PlayMTFSound(n\sounds[0], n)
							;PlayMTFSound(MTFSFX(Rand(0,3)),n)
						EndIf
					EndIf

					n\lastSeen = Int(70*Rnd(30,40))
					n\lastDist = 1

					n\state = 1
					n\enemyX = EntityX(mainPlayer\collider,True)
					n\enemyY = EntityY(mainPlayer\collider,True)
					n\enemyZ = EntityZ(mainPlayer\collider,True)
					n\state2 = 70.0*(15.0*temp) ;give up after 15 seconds (30 seconds if detected by loud noise, over camera: 45)
					DebugLog("player spotted :"+Str(n\state2))
					n\pathTimer=0.0
					n\pathStatus=0
					n\reload = 200-(100*SelectedDifficulty\aggressiveNPCs)

					;If EntityDistance(n\collider,mainPlayer\collider)>HideDistance*0.7
					;	TeleportMTFGroup(n)
					;EndIf
                EndIf

				;B3D doesn't do short-circuit evaluation, so this retarded nesting is an optimization
                If (Curr173\idle<2) Then
					soundVol173 = Max(Min((Distance(EntityX(Curr173\collider), EntityZ(Curr173\collider), Curr173\prevX, Curr173\prevZ) * 2.5), 1.0), 0.0)
					If (OtherNPCSeesMeNPC(Curr173,n) Or (soundVol173>0.0 And EntityDistance(n\collider,Curr173\collider)<6.0)) Then
						If (EntityVisible(n\collider,Curr173\collider) Or soundVol173>0.0) Then
							n\state = 2
							n\enemyX = EntityX(Curr173\collider,True)
							n\enemyY = EntityY(Curr173\collider,True)
							n\enemyZ = EntityZ(Curr173\collider,True)
							n\state2 = Int(70.0*15.0) ;give up after 15 seconds
							n\state3 = 0.0
							n\pathTimer=0.0
							n\pathStatus=0
							DebugLog("173 spotted :"+Str(n\state2))
							If (n\sounds[0] <> 0) Then
								FreeSound(n\sounds[0])
								n\sounds[0] = 0
							EndIf
							n\sounds[0] = LoadSound("SFX/Character/MTF/173/Spotted"+Str(Rand(1,2))+".ogg")
							PlayMTFSound(n\sounds[0], n)
						EndIf
					EndIf
				EndIf

				If (Curr106\state <= 0) Then
					If (OtherNPCSeesMeNPC(Curr106,n) Or EntityDistance(n\collider,Curr106\collider)<3.0) Then
						If (EntityVisible(n\collider,Curr106\collider)) Then
							n\state = 4
							n\enemyX = EntityX(Curr106\collider,True)
							n\enemyY = EntityY(Curr106\collider,True)
							n\enemyZ = EntityZ(Curr106\collider,True)
							n\state2 = 70*15.0
							n\state3 = 0.0
							n\pathTimer = 0.0
							n\pathStatus = 0
							n\target = Curr106
							DebugLog("106 spotted :"+Str(n\state2))
							;If n\mtfLeader=Null
							If (n\sounds[0] <> 0) Then
								FreeSound(n\sounds[0])
								n\sounds[0] = 0
							EndIf
							n\sounds[0] = LoadSound("SFX/Character/MTF/106/Spotted"+Str(Rand(1,3))+".ogg")
								PlayMTFSound(n\sounds[0], n)
							;EndIf
						EndIf
					EndIf
				EndIf

				If (Curr096 <> Null) Then
					If (OtherNPCSeesMeNPC(Curr096,n)) Then
						If (EntityVisible(n\collider,Curr096\collider)) Then
							n\state = 8
							n\enemyX = EntityX(Curr096\collider,True)
							n\enemyY = EntityY(Curr096\collider,True)
							n\enemyZ = EntityZ(Curr096\collider,True)
							n\state2 = 70*15.0
							n\state3 = 0.0
							n\pathTimer = 0.0
							n\pathStatus = 0
							DebugLog("096 spotted :"+Str(n\state2))
							;If n\mtfLeader=Null
								If (n\sounds[0] <> 0) Then
									FreeSound(n\sounds[0])
									n\sounds[0] = 0
								EndIf
								n\sounds[0] = LoadSound("SFX/Character/MTF/096/Spotted"+Str(Rand(1,2))+".ogg")
								PlayMTFSound(n\sounds[0], n)
							;EndIf
						EndIf
					EndIf
				EndIf

				For n2 = Each NPC
					If (n2\npcType = NPCtype049) Then
						If (OtherNPCSeesMeNPC(n2,n)) Then
							If (EntityVisible(n\collider,n2\collider)) Then
								n\state = 4
								n\enemyX = EntityX(n2\collider,True)
								n\enemyY = EntityY(n2\collider,True)
								n\enemyZ = EntityZ(n2\collider,True)
								n\state2 = 70*15.0
								n\state3 = 0.0
								n\pathTimer = 0.0
								n\pathStatus = 0
								n\target = n2
								DebugLog("049 spotted :"+Str(n\state2))
								;If n\mtfLeader=Null
								;	If (n\sounds[0] <> 0) Then FreeSound(n\sounds[0] : n\sounds[0] = 0)
								;	n\sounds[0] = LoadSound("SFX/Character/MTF/"
								;	PlayMTFSound(n\sounds[0], n)
								;EndIf
								Exit
							EndIf
						EndIf
					ElseIf (n2\npcType = NPCtypeZombie And n2\isDead = False) Then
						If (OtherNPCSeesMeNPC(n2,n)) Then
							If (EntityVisible(n\collider,n2\collider)) Then
								n\state = 9
								n\enemyX = EntityX(n2\collider,True)
								n\enemyY = EntityY(n2\collider,True)
								n\enemyZ = EntityZ(n2\collider,True)
								n\state2 = 70*15.0
								n\state3 = 0.0
								n\pathTimer = 0.0
								n\pathStatus = 0
								n\target = n2
								n\reload = 70*5
								DebugLog("049-2 spotted :"+Str(n\state2))
								If (n\sounds[0] <> 0) Then
									FreeSound(n\sounds[0])
									n\sounds[0] = 0
								EndIf
								n\sounds[0] = LoadSound("SFX/Character/MTF/049/Player0492_1.ogg")
								PlayMTFSound(n\sounds[0], n)
								Exit
							EndIf
						EndIf
					EndIf
				Next
                ;[End Block]
			Case 1 ;searching for player
                ;[Block]
                n\speed = 0.015
                n\state2=n\state2-timing\tickDuration
                If (MeNPCSeesPlayer(n) = True) Then

					;if close enough, start shooting at the player
					If (n\playerDistance < 4.0) Then

						angle = VectorYaw(EntityX(mainPlayer\collider)-EntityX(n\collider),0,EntityZ(mainPlayer\collider)-EntityZ(n\collider))

						RotateEntity(n\collider, 0, CurveAngle(angle, EntityYaw(n\collider), 10.0), 0, True)
						n\angle = EntityYaw(n\collider)

						If (n\reload =< 0 And (Not mainPlayer\dead)) Then
							If (EntityVisible(n\collider, mainPlayer\cam)) Then
								angle = WrapAngle(angle - EntityYaw(n\collider))
								If (angle < 5 Or angle > 355) Then
									prev = (Not mainPlayer\dead)

									PlayRangedSound_SM(sndManager\gunshot[0], mainPlayer\cam, n\collider, 15)

									pvt = CreatePivot()

									RotateEntity(pvt, EntityPitch(n\collider), EntityYaw(n\collider), 0, True)
									PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
									MoveEntity(pvt,0.8*0.079, 10.75*0.079, 6.9*0.079)

									Shoot(EntityX(pvt),EntityY(pvt),EntityZ(pvt),5.0/n\playerDistance, False)
									n\reload = 7

									FreeEntity(pvt)

									DeathMSG="Subject D-9341. Died of blood loss after being shot by Nine-Tailed Fox."

									;player killed -> "target terminated"
									If ((Not prev) And mainPlayer\dead) Then
										DeathMSG="Subject D-9341. Terminated by Nine-Tailed Fox."
										PlayMTFSound(LoadTempSound("SFX/Character/MTF/Targetterminated"+Str(Rand(1,4))+".ogg"),n)
									EndIf
								EndIf
							EndIf
						EndIf

						For n2 = Each NPC
							If (n2\npcType = NPCtypeMTF And n2 <> n) Then
								If (n2\state = 0) Then
									If (EntityDistance(n\collider,n2\collider)<6.0) Then
										n\prevState = 1
										n2\lastSeen = Int(70*Rnd(30,40))
										n2\lastDist = 1

										n2\state = 1
										n2\enemyX = EntityX(mainPlayer\collider,True)
										n2\enemyY = EntityY(mainPlayer\collider,True)
										n2\enemyZ = EntityZ(mainPlayer\collider,True)
										n2\state2 = n\state2
										n2\pathTimer=0.0
										n2\pathStatus=0
										n2\reload = 200-(100*SelectedDifficulty\aggressiveNPCs)
										n2\prevState = 0
									EndIf
								EndIf
							EndIf
						Next

						If (n\prevState = 1) Then
							SetNPCFrame(n,423)
							n\prevState = 2
						ElseIf (n\prevState=2) Then
							If (n\frame>200) Then
								n\currSpeed = CurveValue(0, n\currSpeed, 20.0)
								AnimateNPC(n, 423, 463, 0.4, False)
								If (n\frame>462.9) Then n\frame = 78
							Else
								AnimateNPC(n, 78, 193, 0.2, False)
								n\currSpeed = CurveValue(0, n\currSpeed, 20.0)
							EndIf
						Else
							If (n\frame>958) Then
								AnimateNPC(n, 1374, 1383, 0.3, False)
								n\currSpeed = CurveValue(0, n\currSpeed, 20.0)
								If (n\frame>1382.9) Then n\frame = 78
							Else
								AnimateNPC(n, 78, 193, 0.2, False)
								n\currSpeed = CurveValue(0, n\currSpeed, 20.0)
							EndIf
						EndIf
					Else
						PositionEntity(n\obj,n\enemyX,n\enemyY,n\enemyZ,True)
						PointEntity(n\collider,n\obj)
						RotateEntity(n\collider,0.0,EntityYaw(n\collider,True),0.0,True)
						n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
						RotateEntity(n\obj,-90.0,n\angle,0.0,True)

						n\currSpeed = CurveValue(n\speed,n\currSpeed,20.0)
						TranslateEntity(n\collider, Cos(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, 0, Sin(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, True)
						AnimateNPC(n,488, 522, n\currSpeed*26)
					EndIf
                Else
					n\lastSeen = Int(n\lastSeen - timing\tickDuration)

					n\reload = 200-(100*SelectedDifficulty\aggressiveNPCs)

					If (n\pathTimer<=0.0) Then ;update path
						n\pathStatus = FindPath(n,n\enemyX,n\enemyY+0.1,n\enemyZ)
						n\pathTimer = 70.0 * Rnd(6.0,10.0) ;search again after 6 seconds
					ElseIf (n\pathTimer<=70.0 * 2.5) Then
						n\pathTimer=n\pathTimer-timing\tickDuration
						n\currSpeed = 0.0
						If (Rand(1,35)=1) Then
							RotateEntity(n\collider,0.0,Rnd(360.0),0.0,True)
						EndIf
						FinishWalking(n,488,522,n\speed*26)
						n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
						RotateEntity(n\obj,-90.0,n\angle,0.0,True)
					Else
						If (n\pathStatus=2) Then
							n\pathTimer=n\pathTimer-(timing\tickDuration*2.0) ;timer goes down fast
							n\currSpeed = 0.0
							If (Rand(1,35)=1) Then
								RotateEntity(n\collider,0.0,Rnd(360.0),0.0,True)
							EndIf
							FinishWalking(n,488,522,n\speed*26)
							n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
							RotateEntity(n\obj,-90.0,n\angle,0.0,True)
						ElseIf (n\pathStatus=1) Then
							If (n\path[n\pathLocation]=Null) Then
								If (n\pathLocation > 19) Then
									n\pathLocation = 0 : n\pathStatus = 0
								Else
									n\pathLocation = n\pathLocation + 1
								EndIf
							Else
								prevDist = EntityDistance(n\collider,n\path[n\pathLocation]\obj)

								PointEntity(n\collider,n\path[n\pathLocation]\obj)
								RotateEntity(n\collider,0.0,EntityYaw(n\collider,True),0.0,True)
								n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
								RotateEntity(n\obj,-90.0,n\angle,0.0,True)

								n\currSpeed = CurveValue(n\speed,n\currSpeed,20.0)

								TranslateEntity(n\collider, Cos(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, 0, Sin(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, True)
								AnimateNPC(n,488, 522, n\currSpeed*26)

								newDist = EntityDistance(n\collider,n\path[n\pathLocation]\obj)

								;TODO: fix
								;If (newDist<1.0 And n\path[n\pathLocation]\door<>Null) Then
								;	;open the door and make it automatically close after 5 seconds
								;	If (Not n\path[n\pathLocation]\door\open)
								;		sound = 0
								;		If (n\path[n\pathLocation]\door\dir = 1) Then sound = 0 Else sound=Rand(0, 2)
								;		;PlayRangedSound(OpenDoorSFX(n\path[n\pathLocation]\door\dir,sound),mainPlayer\cam,n\path[n\pathLocation]\door\obj)
								;		PlayMTFSound(MTFSFX(5),n)
								;	EndIf
								;	n\path[n\pathLocation]\door\open = True
								;	If n\path[n\pathLocation]\door\mtfClose
								;		n\path[n\pathLocation]\door\timerstate = 70.0*5.0
								;	EndIf
								;EndIf

								If (newDist<0.2) Or ((prevDist<newDist) And (prevDist<1.0)) Then
									n\pathLocation=n\pathLocation+1
								EndIf
							EndIf
							n\pathTimer=n\pathTimer-timing\tickDuration ;timer goes down slow
						Else
							PositionEntity(n\obj,n\enemyX,n\enemyY,n\enemyZ,True)
							If (Distance(EntityX(n\collider,True),EntityZ(n\collider,True),n\enemyX,n\enemyZ)<0.2) Or (Not EntityVisible(n\obj,n\collider)) Then
								If (Rand(1,35)=1) Then
									RotateEntity(n\collider,0.0,Rnd(360.0),0.0,True)
								EndIf
								FinishWalking(n,488,522,n\speed*26)
								If (Rand(1,35)=1) Then
									For wp = Each WayPoint
										If (Rand(1,3)=1) Then
											If (EntityDistance(wp\obj,n\collider)<6.0) Then
												n\enemyX = EntityX(wp\obj,True)
												n\enemyY = EntityY(wp\obj,True)
												n\enemyZ = EntityZ(wp\obj,True)
												n\pathTimer = 0.0
												Exit
											EndIf
										EndIf
									Next
								EndIf
								n\pathTimer=n\pathTimer-timing\tickDuration ;timer goes down slow
							Else
								PointEntity(n\collider,n\obj)
								RotateEntity(n\collider,0.0,EntityYaw(n\collider,True),0.0,True)
								n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
								RotateEntity(n\obj,-90.0,n\angle,0.0,True)

								n\currSpeed = CurveValue(n\speed,n\currSpeed,20.0)
								TranslateEntity(n\collider, Cos(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, 0, Sin(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, True)
								AnimateNPC(n,488, 522, n\currSpeed*26)
							EndIf
						EndIf
					EndIf

					If (n\mtfLeader=Null And n\lastSeen<70*30 And n\lastSeen+timing\tickDuration=>70*30) Then
						If (Rand(2)=1) Then
							PlayMTFSound(LoadTempSound("SFX/Character/MTF/Searching"+Str(Rand(1,6))+".ogg"),n)
						EndIf
					EndIf

					;If EntityDistance(n\collider,mainPlayer\collider)>HideDistance*0.7
					;	TeleportMTFGroup(n)
					;EndIf
                EndIf

                If (n\state2<=0.0 And n\state2+timing\tickDuration >0.0) Then
					If (n\mtfLeader = Null) Then
						DebugLog("targetlost: "+Str(n\state2))
						PlayMTFSound(LoadTempSound("SFX/Character/MTF/Targetlost"+Str(Rand(1,3))+".ogg"),n)
					EndIf
					n\state = 0
                EndIf

				;B3D doesn't do short-circuit evaluation, so this retarded nesting is an optimization
                If (Curr173\idle<2) Then
					soundVol173 = Max(Min((Distance(EntityX(Curr173\collider), EntityZ(Curr173\collider), Curr173\prevX, Curr173\prevZ) * 2.5), 1.0), 0.0)
					If (OtherNPCSeesMeNPC(Curr173,n) Or (soundVol173>0.0 And EntityDistance(n\collider,Curr173\collider)<6.0)) Then
						If (EntityVisible(n\collider,Curr173\collider) Or soundVol173>0.0) Then
							n\state = 2
							n\enemyX = EntityX(Curr173\collider,True)
							n\enemyY = EntityY(Curr173\collider,True)
							n\enemyZ = EntityZ(Curr173\collider,True)
							n\state2 = 70.0*15.0 ;give up after 15 seconds
							DebugLog("173 spotted :"+Str(n\state2))
							If (n\sounds[0] <> 0) Then
								FreeSound(n\sounds[0])
								n\sounds[0] = 0
							EndIf
							n\sounds[0] = LoadSound("SFX/Character/MTF/173/Spotted3.ogg")
							PlayMTFSound(n\sounds[0], n)
							n\state3 = 0.0
							n\pathTimer=0.0
							n\pathStatus=0
						EndIf
					EndIf
				EndIf

				If (Curr106\state <= 0) Then
					If (OtherNPCSeesMeNPC(Curr106,n) Or EntityDistance(n\collider,Curr106\collider)<3.0) Then
						If (EntityVisible(n\collider,Curr106\collider)) Then
							n\state = 4
							n\enemyX = EntityX(Curr106\collider,True)
							n\enemyY = EntityY(Curr106\collider,True)
							n\enemyZ = EntityZ(Curr106\collider,True)
							n\state2 = 70*15.0
							n\state3 = 0.0
							n\pathTimer = 0.0
							n\pathStatus = 0
							n\target = Curr106
							DebugLog("106 spotted :"+Str(n\state2))
							If (n\mtfLeader=Null) Then
								If (n\sounds[0] <> 0) Then
									FreeSound(n\sounds[0])
									n\sounds[0] = 0
								EndIf
								n\sounds[0] = LoadSound("SFX/Character/MTF/106/Spotted4.ogg")
								PlayMTFSound(n\sounds[0], n)
							EndIf
						EndIf
					EndIf
				EndIf

				If (Curr096 <> Null) Then
					If (OtherNPCSeesMeNPC(Curr096,n)) Then
						If (EntityVisible(n\collider,Curr096\collider)) Then
							n\state = 8
							n\enemyX = EntityX(Curr096\collider,True)
							n\enemyY = EntityY(Curr096\collider,True)
							n\enemyZ = EntityZ(Curr096\collider,True)
							n\state2 = 70*15.0
							n\state3 = 0.0
							n\pathTimer = 0.0
							n\pathStatus = 0
							DebugLog("096 spotted :"+Str(n\state2))
							If (n\mtfLeader=Null) Then
								If (n\sounds[0] <> 0) Then
									FreeSound(n\sounds[0])
									n\sounds[0] = 0
								EndIf
								n\sounds[0] = LoadSound("SFX/Character/MTF/096/Spotted"+Str(Rand(1,2))+".ogg")
								PlayMTFSound(n\sounds[0], n)
							EndIf
						EndIf
					EndIf
				EndIf

				For n2 = Each NPC
					If (n2\npcType = NPCtype049) Then
						If (OtherNPCSeesMeNPC(n2,n)) Then
							If (EntityVisible(n\collider,n2\collider)) Then
								n\state = 4
								n\enemyX = EntityX(n2\collider,True)
								n\enemyY = EntityY(n2\collider,True)
								n\enemyZ = EntityZ(n2\collider,True)
								n\state2 = 70*15.0
								n\state3 = 0.0
								n\pathTimer = 0.0
								n\pathStatus = 0
								n\target = n2
								DebugLog("049 spotted :"+Str(n\state2))
								;If n\mtfLeader=Null
								;	If (n\sounds[0] <> 0) Then FreeSound(n\sounds[0] : n\sounds[0] = 0)
								;	n\sounds[0] = LoadSound("SFX/Character/MTF/"
								;	PlayMTFSound(n\sounds[0], n)
								;EndIf
								Exit
							EndIf
						EndIf
					ElseIf (n2\npcType = NPCtypeZombie And n2\isDead = False) Then
						If (OtherNPCSeesMeNPC(n2,n)) Then
							If (EntityVisible(n\collider,n2\collider)) Then
								n\state = 9
								n\enemyX = EntityX(n2\collider,True)
								n\enemyY = EntityY(n2\collider,True)
								n\enemyZ = EntityZ(n2\collider,True)
								n\state2 = 70*15.0
								n\state3 = 0.0
								n\pathTimer = 0.0
								n\pathStatus = 0
								n\target = n2
								n\reload = 70*5
								DebugLog("049-2 spotted :"+Str(n\state2))
								;If n\mtfLeader=Null
								If (n\sounds[0] <> 0) Then
									FreeSound(n\sounds[0])
									n\sounds[0] = 0
								EndIf
									n\sounds[0] = LoadSound("SFX/Character/MTF/049/Player0492_1.ogg")
									PlayMTFSound(n\sounds[0], n)
								;EndIf
								Exit
							EndIf
						EndIf
					EndIf
				Next

                ;DebugLog(Distance(EntityX(n\collider,True),EntityZ(n\collider,True),n\enemyX,n\enemyZ))

                ;[End Block]
			Case 2 ;searching for/looking at 173
                ;[Block]
                If (Curr173\idle = 2) Then
					n\state = 0
                Else
					For n2 = Each NPC
						If (n2<>n) Then
							If (n2\npcType = NPCtypeMTF) Then
								n2\state = 2
							EndIf
						EndIf
					Next

					curr173Dist = Distance(EntityX(n\collider,True),EntityZ(n\collider,True),EntityX(Curr173\collider,True),EntityZ(Curr173\collider,True))

					If (curr173Dist<5.0) Then
						If (Curr173\idle <> 2) Then Curr173\idle = True
						n\state2 = 70.0*15.0
						n\pathTimer = 0.0
						tempDist = 1.0
						If (n\mtfLeader<>Null) Then tempDist = 2.0
						If (curr173Dist<tempDist) Then
							If (n\mtfLeader = Null) Then
								n\state3=n\state3+timing\tickDuration
								DebugLog("CONTAINING 173: "+Str(n\state3))
								;If (n\state3>=70.0*10.0) Then
								If (n\state3>=70.0*15.0) Then
									Curr173\idle = 2
									If (n\mtfLeader = Null) Then Curr173\target = n
									If (n\sounds[0] <> 0) Then
										FreeSound(n\sounds[0])
										n\sounds[0] = 0
									EndIf
									n\sounds[0] = LoadSound("SFX/Character/MTF/173/Box"+Str(Rand(1,3))+".ogg")
									PlayMTFSound(n\sounds[0], n)
								EndIf
							EndIf
							PositionEntity(n\obj,EntityX(Curr173\collider,True),EntityY(Curr173\collider,True),EntityZ(Curr173\collider,True),True)
							PointEntity(n\collider,n\obj)
							RotateEntity(n\collider,0.0,EntityYaw(n\collider,True),0.0,True)
							n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
							FinishWalking(n,488,522,n\speed*26)
							RotateEntity(n\obj,-90.0,n\angle,0.0,True)
						Else
							PositionEntity(n\obj,EntityX(Curr173\collider,True),EntityY(Curr173\collider,True),EntityZ(Curr173\collider,True),True)
							PointEntity(n\collider,n\obj)
							RotateEntity(n\collider,0.0,EntityYaw(n\collider,True),0.0,True)
							n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
							RotateEntity(n\obj,-90.0,n\angle,0.0,True)

							n\currSpeed = CurveValue(n\speed,n\currSpeed,20.0)
							TranslateEntity(n\collider, Cos(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, 0, Sin(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, True)
							AnimateNPC(n,488, 522, n\currSpeed*26)
						EndIf
					Else
						If (Curr173\idle <> 2) Then Curr173\idle = False
						If (n\pathTimer<=0.0) Then ;update path
							n\pathStatus = FindPath(n,EntityX(Curr173\collider,True),EntityY(Curr173\collider,True)+0.1,EntityZ(Curr173\collider,True))
							n\pathTimer = 70.0 * Rnd(6.0,10.0) ;search again after 6 seconds
						ElseIf (n\pathTimer<=70.0 * 2.5) Then
							n\pathTimer=n\pathTimer-timing\tickDuration
							n\currSpeed = 0.0
							If (Rand(1,35)=1) Then
								RotateEntity(n\collider,0.0,Rnd(360.0),0.0,True)
							EndIf
							FinishWalking(n,488,522,n\speed*26)
							n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
							RotateEntity(n\obj,-90.0,n\angle,0.0,True)
						Else
							If (n\pathStatus=2) Then
								n\pathTimer=n\pathTimer-(timing\tickDuration*2.0) ;timer goes down fast
								n\currSpeed = 0.0
								If (Rand(1,35)=1) Then
									RotateEntity(n\collider,0.0,Rnd(360.0),0.0,True)
								EndIf
								FinishWalking(n,488,522,n\speed*26)
								n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
								RotateEntity(n\obj,-90.0,n\angle,0.0,True)
							ElseIf (n\pathStatus=1) Then
								If (n\path[n\pathLocation]=Null) Then
									If (n\pathLocation > 19) Then
										n\pathLocation = 0 : n\pathStatus = 0
									Else
										n\pathLocation = n\pathLocation + 1
									EndIf
								Else
									prevDist = EntityDistance(n\collider,n\path[n\pathLocation]\obj)

									PointEntity(n\collider,n\path[n\pathLocation]\obj)
									RotateEntity(n\collider,0.0,EntityYaw(n\collider,True),0.0,True)
									n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
									RotateEntity(n\obj,-90.0,n\angle,0.0,True)

									n\currSpeed = CurveValue(n\speed,n\currSpeed,20.0)

									TranslateEntity(n\collider, Cos(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, 0, Sin(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, True)
									AnimateNPC(n,488, 522, n\currSpeed*26)

									newDist = EntityDistance(n\collider,n\path[n\pathLocation]\obj)

									;TODO: fix
									;If (newDist<1.0 And n\path[n\pathLocation]\door<>Null) Then
									;	;open the door and make it automatically close after 5 seconds
									;	If (Not n\path[n\pathLocation]\door\open)
									;		sound = 0
									;		If (n\path[n\pathLocation]\door\dir = 1) Then sound = 0 Else sound=Rand(0, 2)
									;		;PlayRangedSound(OpenDoorSFX(n\path[n\pathLocation]\door\dir,sound),mainPlayer\cam,n\path[n\pathLocation]\door\obj)
									;		PlayMTFSound(MTFSFX(5),n)
									;	EndIf
									;	n\path[n\pathLocation]\door\open = True
									;	If n\path[n\pathLocation]\door\mtfClose
									;		n\path[n\pathLocation]\door\timerstate = 70.0*5.0
									;	EndIf
									;EndIf

									If (newDist<0.2) Or ((prevDist<newDist) And (prevDist<1.0)) Then
										n\pathLocation=n\pathLocation+1
									EndIf
								EndIf
								n\pathTimer=n\pathTimer-timing\tickDuration ;timer goes down slow
							Else
								n\pathTimer=n\pathTimer-(timing\tickDuration*2.0) ;timer goes down fast
								n\currSpeed = 0.0
								If (Rand(1,35)=1) Then
									RotateEntity(n\collider,0.0,Rnd(360.0),0.0,True)
								EndIf
								FinishWalking(n,488,522,n\speed*26)
								n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
								RotateEntity(n\obj,-90.0,n\angle,0.0,True)
							EndIf
						EndIf
					EndIf
                EndIf
                ;[End Block]
			Case 3 ;following a path
				;[Block]

				n\angle = CurveValue(0,n\angle,40.0)

				If (n\pathStatus = 2) Then
					n\state = 5
					n\currSpeed = 0
				ElseIf (n\pathStatus = 1) Then
					If (n\path[n\pathLocation]=Null) Then
						If (n\pathLocation > 19) Then
							n\pathLocation = 0
							n\pathStatus = 0
							If (n\lastSeen > 0) Then n\state = 5
						Else
							n\pathLocation = n\pathLocation + 1
						EndIf
					Else
						;TODO: fix
						;If (n\path[n\pathLocation]\door <> Null) Then
						;	If (n\path[n\pathLocation]\door\open = False) Then
						;		n\path[n\pathLocation]\door\open = True
						;		n\path[n\pathLocation]\door\timerstate = 8.0*70.0
						;		PlayMTFSound(MTFSFX(5),n)
						;	EndIf
						;EndIf

						If (dist < HideDistance*0.7) Then
							dist2 = EntityDistance(n\collider,n\path[n\pathLocation]\obj)

							;If (Rand(5)=1) Then
							;	For n2.NPC = Each NPC
							;		If (n2\npctype = n\npcType And n2<>n) Then
							;			If (EntityDistance(n\collider, n2\collider)<2) Then
							;				n\idle = 150
							;			EndIf
							;		EndIf
							;	Next
							;EndIf

							PointEntity(n\obj, n\path[n\pathLocation]\obj)

							RotateEntity(n\collider, 0, CurveAngle(EntityYaw(n\obj), EntityYaw(n\collider), 10.0), 0)
							If (n\idle = 0) Then
								n\currSpeed = CurveValue(n\speed*Max(Min(dist2,1.0),0.1), n\currSpeed, 20.0)
								MoveEntity(n\collider, 0, 0, n\currSpeed * timing\tickDuration)

								;If (dist2 < (0.25+((n\path[Min(n\pathLocation+1,19)]=Null)*0.3 * (n\id Mod 3)))) Then
								If (EntityDistance(n\collider,n\path[n\pathLocation]\obj)<0.5) Then
									n\pathLocation = n\pathLocation + 1
								EndIf
							EndIf
						Else
							If (Rand(20)=1) Then
								PositionEntity(n\collider, EntityX(n\path[n\pathLocation]\obj,True),EntityY(n\path[n\pathLocation]\obj,True)+0.25,EntityZ(n\path[n\pathLocation]\obj,True),True)
								n\pathLocation = n\pathLocation + 1
								ResetEntity(n\collider)
							EndIf
						EndIf

					EndIf
				Else
					n\currSpeed = 0
					n\state = 5
				EndIf


				If (n\idle = 0 And n\pathStatus = 1) Then
					If (dist < HideDistance) Then
						If (n\frame>959) Then
							AnimateNPC(n, 1376, 1383, 0.2, False)
							If (n\frame >1382.9) Then n\frame = 488
						Else
							AnimateNPC(n, 488, 522, n\currSpeed*30)
						EndIf
					EndIf
				Else
					If (dist < HideDistance) Then
						If (n\lastSeen > 0) Then
							AnimateNPC(n, 78, 312, 0.2, True)
						Else
							If (n\frame<962) Then
								If (n\frame>487) Then n\frame = 463
								AnimateNPC(n, 463, 487, 0.3, False)
								If (n\frame>486.9) Then n\frame = 962
							Else
								AnimateNPC(n, 962, 1259, 0.3)
							EndIf
						EndIf
					EndIf

					n\currSpeed = CurveValue(0, n\currSpeed, 20.0)
				EndIf

				n\angle = EntityYaw(n\collider)
				;[End Block]
			Case 4 ;SCP-106/049 detected
				;[Block]
				n\speed = 0.03
                n\state2=n\state2-timing\tickDuration
				If (n\state2 > 0.0) Then
					If (OtherNPCSeesMeNPC(n\target,n)) Then
						n\state2 = 70*15
					EndIf

					If (EntityDistance(n\target\collider,n\collider)>HideDistance) Then
						If (n\state2 > 70) Then
							n\state2 = 70
						EndIf
					EndIf

					If (EntityDistance(n\target\collider,n\collider)<3.0 And n\state3 >= 0.0) Then
						n\state3 = 70*5
					EndIf

					If (n\state3 > 0.0) Then
						n\pathStatus = 0
						n\pathLocation = 0
						n\speed = 0.02
						PointEntity(n\collider,n\target\collider)
						RotateEntity(n\collider,0.0,EntityYaw(n\collider,True),0.0,True)
						n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
						RotateEntity(n\obj,-90.0,n\angle,0.0,True)
						n\currSpeed = CurveValue(-n\speed,n\currSpeed,20.0)
						TranslateEntity(n\collider, Cos(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, 0, Sin(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, True)
						AnimateNPC(n,522, 488, n\currSpeed*26)

						n\pathTimer = 1.0

						n\state3=Max(n\state3-timing\tickDuration,0)

						HideEntity(n\collider)
						TurnEntity(n\collider,0,180,0)
						EntityPick(n\collider, 1.0)
						If (PickedEntity() <> 0) Then
							n\state3 = -70*2
						EndIf
						ShowEntity(n\collider)
						TurnEntity(n\collider,0,180,0)
					ElseIf (n\state3 < 0.0) Then
						n\state3 = Min(n\state3+timing\tickDuration,0)
					EndIf

					If (n\pathTimer<=0.0) Then
						If (n\mtfLeader<>Null) Then
							n\pathStatus = FindPath(n,EntityX(n\mtfLeader\collider,True),EntityY(n\mtfLeader\collider,True)+0.1,EntityZ(n\mtfLeader\collider,True))
						Else
							For r = Each Room
								If (((Abs(r\x-EntityX(n\collider,True))>12.0) Or (Abs(r\z-EntityZ(n\collider,True))>12.0)) And (Rand(1,Int(Max(4-Int(Abs(r\z-EntityZ(n\collider,True)/8.0)),2)))=1)) Then
									If (EntityDistance(r\obj,n\target\collider)>6.0) Then
										x = r\x
										y = 0.1
										z = r\z
										DebugLog(r\roomTemplate\name)
										Exit
									EndIf
								EndIf
							Next
							n\pathStatus = FindPath(n,x,y,z)
						EndIf
						If (n\pathStatus = 1) Then
							While n\path[n\pathLocation]=Null
								If (n\pathLocation>19) Then Exit
								n\pathLocation=n\pathLocation+1
							Wend
							If (n\pathLocation<19) Then
								If (n\path[n\pathLocation]<>Null) And (n\path[n\pathLocation+1]<>Null) Then
									;If (n\path[n\pathLocation]\door=Null) Then ;TODO: fix?
									If (Abs(DeltaYaw(n\collider,n\path[n\pathLocation]\obj))>Abs(DeltaYaw(n\collider,n\path[n\pathLocation+1]\obj))) Then
										n\pathLocation=n\pathLocation+1
									EndIf
									;EndIf
								EndIf
							EndIf
						EndIf
						n\pathTimer = 70*10
					Else
						If (n\pathStatus=1) Then
							If (n\path[n\pathLocation]=Null) Then
								If (n\pathLocation > 19) Then
									n\pathLocation = 0 : n\pathStatus = 0
								Else
									n\pathLocation = n\pathLocation + 1
								EndIf
							Else
								prevDist = EntityDistance(n\collider,n\path[n\pathLocation]\obj)

								PointEntity(n\collider,n\path[n\pathLocation]\obj)
								RotateEntity(n\collider,0.0,EntityYaw(n\collider,True),0.0,True)
								n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
								RotateEntity(n\obj,-90.0,n\angle,0.0,True)

								n\currSpeed = CurveValue(n\speed,n\currSpeed,20.0)
								TranslateEntity(n\collider, Cos(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, 0, Sin(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, True)
								AnimateNPC(n,488, 522, n\currSpeed*26) ;Placeholder (until running animation has been implemented)

								newDist = EntityDistance(n\collider,n\path[n\pathLocation]\obj)

								;TODO: fix and remove duplicates maybe???
								;If (newDist<2.0 And n\path[n\pathLocation]\door<>Null) Then
								;	If (Not n\path[n\pathLocation]\door\open)
								;		sound = 0
								;		If (n\path[n\pathLocation]\door\dir = 1) Then sound = 0 Else sound=Rand(0, 2)
								;		;PlayRangedSound(OpenDoorSFX(n\path[n\pathLocation]\door\dir,sound),mainPlayer\cam,n\path[n\pathLocation]\door\obj)
								;		PlayMTFSound(MTFSFX(5),n)
								;	EndIf
								;	n\path[n\pathLocation]\door\open = True
								;	If n\path[n\pathLocation]\door\mtfClose
								;		n\path[n\pathLocation]\door\timerstate = 70.0*5.0
								;	EndIf
								;EndIf

								If (newDist<0.2) Or ((prevDist<newDist) And (prevDist<1.0)) Then
									n\pathLocation=n\pathLocation+1
								EndIf
							EndIf
							n\pathTimer=n\pathTimer-timing\tickDuration
						Else
							n\pathTimer=0.0
						EndIf
					EndIf
				Else
					n\state = 0
				EndIf
				;[End Block]
			Case 5 ;looking at some other target than the player
				;[Block]
				target=CreatePivot()
				PositionEntity(target, n\enemyX, n\enemyY, n\enemyZ, True)

				If (dist<HideDistance) Then
					AnimateNPC(n, 346, 351, 0.2, False)
				EndIf

				If (Abs(EntityX(target)-EntityX(n\collider)) < 55.0 And Abs(EntityZ(target)-EntityZ(n\collider)) < 55.0 And Abs(EntityY(target)-EntityY(n\collider))< 20.0) Then

					PointEntity(n\obj, target)
					RotateEntity(n\collider, 0, CurveAngle(EntityYaw(n\obj),EntityYaw(n\collider),30.0), 0, True)

					If (n\pathTimer = 0) Then
						n\pathStatus = EntityVisible(n\collider,target)
						n\pathTimer = Rand(100,200)
					Else
						n\pathTimer = Min(n\pathTimer-timing\tickDuration,0.0)
					EndIf

					If (n\pathStatus = 1 And n\reload =< 0) Then
						dist = Distance(EntityX(target),EntityZ(target),EntityX(n\collider),EntityZ(n\collider))

						;If (dist<20.0) Then
						;	pvt = CreatePivot()
						;
						;	PositionEntity(pvt, EntityX(n\obj),EntityY(n\obj), EntityZ(n\obj))
						;	RotateEntity(pvt, EntityPitch(n\collider), EntityYaw(n\collider),0)
						;	MoveEntity(pvt,0.8*0.079, 10.75*0.079, 6.9*0.079)
						;
						;	If (WrapAngle(EntityYaw(pvt)-EntityYaw(n\collider))<5) Then
						;		PlayRangedSound(GunshotSFX, mainPlayer\cam, n\collider, 20)
						;		p.Particle = CreateParticle(EntityX(n\obj, True), EntityY(n\obj, True), EntityZ(n\obj, True), 1, 0.2, 0.0, 5)
						;		PositionEntity(p\pvt, EntityX(pvt), EntityY(pvt), EntityZ(pvt))
						;
						;		n\reload = 7
						;	EndIf
						;
						;	FreeEntity(pvt)
						;EndIf
					EndIf
				EndIf

				FreeEntity(target)

				n\angle = EntityYaw(n\collider)
				;[End Block]
			Case 6 ;seeing the player as a 049-2 instance
				;[Block]

				PointEntity(n\obj,mainPlayer\collider)

				RotateEntity(n\collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\collider),20.0),0)
				n\angle = EntityYaw(n\collider)

				AnimateNPC(n, 346, 351, 0.2, False)

				If (n\reload =< 0 And (Not mainPlayer\dead)) Then
					If (EntityVisible(n\collider, mainPlayer\collider)) Then
						;angle# = WrapAngle(angle - EntityYaw(n\collider))
						;If (angle < 5 Or angle > 355) Then
						If (Abs(DeltaYaw(n\collider,mainPlayer\collider))<50.0) Then
							;prev% = KillTimer

							PlayRangedSound_SM(sndManager\gunshot[0], mainPlayer\cam, n\collider, 15)

							pvt = CreatePivot()

							RotateEntity(pvt, EntityPitch(n\collider), EntityYaw(n\collider), 0, True)
							PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
							MoveEntity(pvt,0.8*0.079, 10.75*0.079, 6.9*0.079)

							Shoot(EntityX(pvt),EntityY(pvt),EntityZ(pvt),0.9, False)
							n\reload = 7

							FreeEntity(pvt)

							;If (prev => 0 And KillTimer < 0) Then
								;DeathMSG="Subject D-9341. Terminated by Nine-Tailed Fox."
								;If (n\mtfLeader = Null) Then PlayMTFSound(LoadTempSound("SFX/Character/MTF/049/Player0492_2.ogg"),n)
							;EndIf
						EndIf
					EndIf
				EndIf

				;[End Block]
			Case 7 ;just shooting
				;[Block]
				AnimateNPC(n, 346, 351, 0.2, False)

				RotateEntity(n\collider,0,CurveAngle(n\state2,EntityYaw(n\collider),20),0)
				n\angle = EntityYaw(n\collider)

				If (n\reload =< 0) Then
					;LightVolume = TempLightVolume*1.2
					PlayRangedSound_SM(sndManager\gunshot[0], mainPlayer\cam, n\collider, 20)

					pvt = CreatePivot()

					RotateEntity(pvt, EntityPitch(n\collider), EntityYaw(n\collider), 0, True)
					PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
					MoveEntity(pvt,0.8*0.079, 10.75*0.079, 6.9*0.079)

					p = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), PARTICLE_FLASH, Rnd(0.08,0.1), 0.0, 5)
					TurnEntity(p\obj, 0,0,Rnd(360))
					p\aChange = -0.15

					FreeEntity(pvt)
					n\reload = 7
				EndIf
				;[End Block]
			Case 8 ;SCP-096 spotted
				;[Block]
				n\speed = 0.015
				n\boneToManipulate = "head"
				n\manipulateBone = True
				n\manipulationType = 2
                If (n\pathTimer<=0.0) Then ;update path
					If (n\mtfLeader<>Null) Then ;i'll follow the leader
						n\pathStatus = FindPath(n,EntityX(n\mtfLeader\collider,True),EntityY(n\mtfLeader\collider,True)+0.1,EntityZ(n\mtfLeader\collider,True)) ;whatever you say boss
					Else ;i am the leader
						For r = Each Room
							If (((Abs(r\x-EntityX(n\collider,True))>12.0) Or (Abs(r\z-EntityZ(n\collider,True))>12.0)) And (Rand(1,Int(Max(4-Int(Abs(r\z-EntityZ(n\collider,True)/8.0)),2)))=1)) Then
								x = r\x
								y = 0.1
								z = r\z
								DebugLog(r\roomTemplate\name)
								Exit
							EndIf
						Next
						n\pathStatus = FindPath(n,x,y,z) ;we're going to this room for no particular reason
					EndIf
					If (n\pathStatus = 1) Then
						While n\path[n\pathLocation]=Null
							If (n\pathLocation>19) Then Exit
							n\pathLocation=n\pathLocation+1
						Wend
						If (n\pathLocation<19) Then
							If (n\path[n\pathLocation]<>Null) And (n\path[n\pathLocation+1]<>Null) Then
								;If (n\path[n\pathLocation]\door=Null) Then ;TODO: fix?
								If (Abs(DeltaYaw(n\collider,n\path[n\pathLocation]\obj))>Abs(DeltaYaw(n\collider,n\path[n\pathLocation+1]\obj))) Then
									n\pathLocation=n\pathLocation+1
								EndIf
								;EndIf
							EndIf
						EndIf
					EndIf
					n\pathTimer = 70.0 * Rnd(6.0,10.0) ;search again after 6-10 seconds
                ElseIf (n\pathTimer<=70.0 * 2.5) And (n\mtfLeader=Null) Then
					n\pathTimer=n\pathTimer-timing\tickDuration
					n\currSpeed = 0.0
					;If (Rand(1,35)=1) Then
					;	RotateEntity(n\collider,0.0,Rnd(360.0),0.0,True)
					;EndIf
					FinishWalking(n,488,522,n\speed*26)
					n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
					RotateEntity(n\obj,-90.0,n\angle,0.0,True)
                Else
					If (n\pathStatus=2) Then
						n\pathTimer=n\pathTimer-(timing\tickDuration*2.0) ;timer goes down fast
						n\currSpeed = 0.0
						;If (Rand(1,35)=1) Then
						;	RotateEntity(n\collider,0.0,Rnd(360.0),0.0,True)
						;EndIf
						FinishWalking(n,488,522,n\speed*26)
						n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
						RotateEntity(n\obj,-90.0,n\angle,0.0,True)
					ElseIf (n\pathStatus=1) Then
						If (n\path[n\pathLocation]=Null) Then
							If (n\pathLocation > 19) Then
								n\pathLocation = 0 : n\pathStatus = 0
							Else
								n\pathLocation = n\pathLocation + 1
							EndIf
						Else
							prevDist = EntityDistance(n\collider,n\path[n\pathLocation]\obj)

							PointEntity(n\collider,n\path[n\pathLocation]\obj)
							RotateEntity(n\collider,0.0,EntityYaw(n\collider,True),0.0,True)
							n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
							RotateEntity(n\obj,-90.0,n\angle,0.0,True)

							n\currSpeed = CurveValue(n\speed,n\currSpeed,20.0)
							;MoveEntity(n\collider, 0, 0, n\currSpeed * timing\tickDuration)
							TranslateEntity(n\collider, Cos(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, 0, Sin(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, True)
							AnimateNPC(n,488, 522, n\currSpeed*26)

							newDist = EntityDistance(n\collider,n\path[n\pathLocation]\obj)

							;TODO: fix AND REMOVE DUPLICATES OMG
							;If (newDist<1.0 And n\path[n\pathLocation]\door<>Null) Then
							;	;open the door and make it automatically close after 5 seconds
							;	If (Not n\path[n\pathLocation]\door\open)
							;		sound = 0
							;		If (n\path[n\pathLocation]\door\dir = 1) Then sound = 0 Else sound=Rand(0, 2)
							;		;PlayRangedSound(OpenDoorSFX(n\path[n\pathLocation]\door\dir,sound),mainPlayer\cam,n\path[n\pathLocation]\door\obj)
							;		PlayMTFSound(MTFSFX(5),n)
							;	EndIf
							;	n\path[n\pathLocation]\door\open = True
							;	If n\path[n\pathLocation]\door\mtfClose
							;		n\path[n\pathLocation]\door\timerstate = 70.0*5.0
							;	EndIf
							;EndIf

							If (newDist<0.2) Or ((prevDist<newDist) And (prevDist<1.0)) Then
								n\pathLocation=n\pathLocation+1
							EndIf
						EndIf
						n\pathTimer=n\pathTimer-timing\tickDuration ;timer goes down slow
					Else
						n\pathTimer=n\pathTimer-(timing\tickDuration*2.0) ;timer goes down fast
						If (n\mtfLeader = Null) Then
							;If (Rand(1,35)=1) Then
							;	RotateEntity(n\collider,0.0,Rnd(360.0),0.0,True)
							;EndIf
							FinishWalking(n,488,522,n\speed*26)
							n\currSpeed = 0.0
						ElseIf (EntityDistance(n\collider,n\mtfLeader\collider)>1.0) Then
							PointEntity(n\collider,n\mtfLeader\collider)
							RotateEntity(n\collider,0.0,EntityYaw(n\collider,True),0.0,True)

							n\currSpeed = CurveValue(n\speed,n\currSpeed,20.0)
							TranslateEntity(n\collider, Cos(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, 0, Sin(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, True)
							AnimateNPC(n,488, 522, n\currSpeed*26)
						Else
							;If (Rand(1,35)=1) Then
							;	RotateEntity(n\collider,0.0,Rnd(360.0),0.0,True)
							;EndIf
							FinishWalking(n,488,522,n\speed*26)
							n\currSpeed = 0.0
						EndIf
						n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
						RotateEntity(n\obj,-90.0,n\angle,0.0,True)
					EndIf
                EndIf

				If ((Not EntityVisible(n\collider,Curr096\collider)) Or EntityDistance(n\collider,Curr096\collider)>6.0) Then
					n\state = 0
				EndIf
				;[End Block]
			Case 9 ;SCP-049-2 spotted
				;[Block]
				If (EntityVisible(n\collider, n\target\collider)) Then
					PointEntity(n\obj,n\target\collider)
					RotateEntity(n\collider,0,CurveAngle(EntityYaw(n\obj),EntityYaw(n\collider),20.0),0)
					n\angle = EntityYaw(n\collider)

					If (EntityDistance(n\target\collider,n\collider)<1.3) Then
						n\state3 = 70*2
					EndIf

					If (n\state3 > 0.0) Then
						n\pathStatus = 0
						n\pathLocation = 0
						n\speed = 0.02
						n\currSpeed = CurveValue(-n\speed,n\currSpeed,20.0)
						TranslateEntity(n\collider, Cos(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, 0, Sin(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, True)
						AnimateNPC(n,522, 488, n\currSpeed*26)

						n\pathTimer = 1.0

						n\state3=Max(n\state3-timing\tickDuration,0)
					Else
						n\state3 = 0
						AnimateNPC(n, 346, 351, 0.2, False)
					EndIf
					If (n\reload =< 0 And n\target\isDead = False) Then
						;angle# = WrapAngle(angle - EntityYaw(n\collider))
						;If (angle < 5 Or angle > 355) Then
						If (Abs(DeltaYaw(n\collider,n\target\collider))<50.0) Then
							;prev% = KillTimer

							PlayRangedSound_SM(sndManager\gunshot[0], mainPlayer\cam, n\collider, 15)

							pvt = CreatePivot()

							RotateEntity(pvt, EntityPitch(n\collider), EntityYaw(n\collider), 0, True)
							PositionEntity(pvt, EntityX(n\obj), EntityY(n\obj), EntityZ(n\obj))
							MoveEntity(pvt,0.8*0.079, 10.75*0.079, 6.9*0.079)

							p = CreateParticle(EntityX(pvt), EntityY(pvt), EntityZ(pvt), PARTICLE_FLASH, Rnd(0.08,0.1), 0.0, 5)
							TurnEntity(p\obj, 0,0,Rnd(360))
							p\aChange = -0.15
							If (n\target\hp > 0) Then
								n\target\hp = Int(Max(n\target\hp-Rand(5,10),0))
							Else
								If (Not n\target\isDead) Then
									If (n\sounds[0] <> 0) Then
										FreeSound(n\sounds[0])
										n\sounds[0] = 0
									EndIf
									n\sounds[0] = LoadSound("SFX/Character/MTF/049/Player0492_2.ogg")
									PlayMTFSound(n\sounds[0], n)
								EndIf
								SetNPCFrame(n\target,133)
								n\target\isDead = True
								n\state = 0
							EndIf
							n\reload = 7

							FreeEntity(pvt)
						EndIf
					EndIf
					n\pathStatus = 0
				Else
					If (n\pathTimer<=0.0) Then
						n\pathStatus = FindPath(n,EntityX(n\target\collider),EntityY(n\target\collider),EntityZ(n\target\collider))
						If (n\pathStatus = 1) Then
							While n\path[n\pathLocation]=Null
								If (n\pathLocation>19) Then Exit
								n\pathLocation=n\pathLocation+1
							Wend
							If (n\pathLocation<19) Then
								If (n\path[n\pathLocation]<>Null) And (n\path[n\pathLocation+1]<>Null) Then
									;If (n\path[n\pathLocation]\door=Null) Then ;TODO: fix
									If (Abs(DeltaYaw(n\collider,n\path[n\pathLocation]\obj))>Abs(DeltaYaw(n\collider,n\path[n\pathLocation+1]\obj))) Then
										n\pathLocation=n\pathLocation+1
									EndIf
									;EndIf
								EndIf
							EndIf
						EndIf
						n\pathTimer = 70*10
					Else
						If (n\pathStatus=1) Then
							If (n\path[n\pathLocation]=Null) Then
								If (n\pathLocation > 19) Then
									n\pathLocation = 0 : n\pathStatus = 0
								Else
									n\pathLocation = n\pathLocation + 1
								EndIf
							Else
								prevDist = EntityDistance(n\collider,n\path[n\pathLocation]\obj)

								PointEntity(n\collider,n\path[n\pathLocation]\obj)
								RotateEntity(n\collider,0.0,EntityYaw(n\collider,True),0.0,True)
								n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)
								RotateEntity(n\obj,-90.0,n\angle,0.0,True)

								n\currSpeed = CurveValue(n\speed,n\currSpeed,20.0)
								TranslateEntity(n\collider, Cos(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, 0, Sin(EntityYaw(n\collider,True)+90.0)*n\currSpeed * timing\tickDuration, True)
								AnimateNPC(n,488, 522, n\currSpeed*26)

								newDist = EntityDistance(n\collider,n\path[n\pathLocation]\obj)

								;TODO: fix
								;If (newDist<1.0 And n\path[n\pathLocation]\door<>Null) Then
								;	If (Not n\path[n\pathLocation]\door\open)
								;		PlayMTFSound(MTFSFX(5),n)
								;	EndIf
								;	n\path[n\pathLocation]\door\open = True
								;	If n\path[n\pathLocation]\door\mtfClose
								;		n\path[n\pathLocation]\door\timerstate = 70.0*5.0
								;	EndIf
								;EndIf

								If (newDist<0.2) Or ((prevDist<newDist) And (prevDist<1.0)) Then
									n\pathLocation=n\pathLocation+1
								EndIf
							EndIf
							n\pathTimer=n\pathTimer-timing\tickDuration
						Else
							n\pathTimer=0.0
						EndIf
					EndIf
				EndIf

				;[End Block]
		End Select

		If (n\currSpeed > 0.01) Then
			If (prevFrame > 500 And n\frame<495) Then
				PlayRangedSound(sndManager\footstepMetal[Rand(0,7)]\internal, mainPlayer\cam, n\collider, 8.0, Rnd(0.5,0.7))
			ElseIf (prevFrame < 505 And n\frame=>505) Then
				PlayRangedSound(sndManager\footstepMetal[Rand(0,7)]\internal, mainPlayer\cam, n\collider, 8.0, Rnd(0.5,0.7))
			EndIf
		EndIf

		If (NoTarget And n\state = 1) Then n\state = 0

		If (n\state <> 3 And n\state <> 5 And n\state <> 6 And n\state <> 7) Then
			If (n\mtfLeader<>Null) Then
				If (EntityDistance(n\collider,n\mtfLeader\collider)<0.7) Then
					PointEntity(n\collider,n\mtfLeader\collider)
					RotateEntity(n\collider,0.0,EntityYaw(n\collider,True),0.0,True)
					n\angle = CurveAngle(EntityYaw(n\collider,True),n\angle,20.0)

					TranslateEntity(n\collider, Cos(EntityYaw(n\collider,True)-45)* 0.01 * timing\tickDuration, 0, Sin(EntityYaw(n\collider,True)-45)* 0.01 * timing\tickDuration, True)
				EndIf
			Else
				For n2 = Each NPC
					If (n2<>n And n2\isDead=False) Then
						If (Abs(DeltaYaw(n\collider,n2\collider))<80.0) Then
							If (EntityDistance(n\collider,n2\collider)<0.7) Then
								TranslateEntity(n2\collider, Cos(EntityYaw(n\collider,True)+90)* 0.01 * timing\tickDuration, 0, Sin(EntityYaw(n\collider,True)+90)* 0.01 * timing\tickDuration, True)
							EndIf
						EndIf
					EndIf
				Next
			EndIf
		EndIf

		;teleport back to the facility if fell through the floor
		If (n\state <> 6 And n\state <> 7) Then
			If (EntityY(n\collider) < -10.0) Then
				TeleportCloser(n)
			EndIf
		EndIf

		RotateEntity(n\obj,-90.0,n\angle,0.0,True)

		PositionEntity(n\obj,EntityX(n\collider,True),EntityY(n\collider,True)-0.15,EntityZ(n\collider,True),True)

	EndIf
End Function

Function UpdateMTF()
	If (mainPlayer\currRoom\roomTemplate\name = "gateaentrance") Then Return

	Local r.Room, n.NPC, leader.NPC
	Local dist#, i%
	Local entrance.Room

	;mtf ei vielä spawnannut, spawnataan jos pelaaja menee tarpeeksi lähelle gate b:tä
	If (MTFtimer = 0) Then
		If (Rand(30)=1 And mainPlayer\currRoom\roomTemplate\name$ <> "dimension1499") Then

			entrance = Null
			For r = Each Room
				If (Lower(r\roomTemplate\name) = "gateaentrance") Then
					entrance = r : Exit
				EndIf
			Next

			If (entrance <> Null) Then
				If (Abs(EntityZ(entrance\obj)-EntityZ(mainPlayer\collider))<30.0) Then
					;If (mainPlayer\currRoom\roomTemplate\name<>"room860" And mainPlayer/currRoom/RoomTemplate/Name<>"pocketdimension") Then
					If (PlayerInReachableRoom()) Then
						PlaySound2(LoadTempSound("SFX/Character/MTF/Announc.ogg"))
					EndIf

					MTFtimer = 1

					For i = 0 To 2
						n = CreateNPC(NPCtypeMTF, EntityX(entrance\obj)+0.3*(i-1), 1.0,EntityZ(entrance\obj)+8.0)

						If (i = 0) Then
							leader = n
						Else
							n\mtfLeader = leader
						EndIf

						n\prevX = i
					Next
				EndIf
			EndIf
		EndIf
	EndIf

End Function
;~IDEal Editor Parameters:
;~C#Blitz3D