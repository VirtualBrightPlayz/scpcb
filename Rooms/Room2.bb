

Function UpdateEventRoom2trick(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		If EntityDistance(e\room\obj,mainPlayer\collider)<2.0 Then
			If EntityDistance(mainPlayer\collider, Curr173\obj)<6.0 Or EntityDistance(mainPlayer\collider, Curr106\obj)<6.0 Then
				RemoveEvent(e)
			Else
				DebugLog "%@@= \ {2E6C2=FD gi`h]c"
				
				pvt = CreatePivot()
				PositionEntity pvt, EntityX(mainPlayer\collider),EntityY(mainPlayer\collider),EntityZ(mainPlayer\collider)
				PointEntity pvt, e\room\obj
				RotateEntity pvt, 0, EntityYaw(pvt),0,True
				MoveEntity pvt, 0,0,EntityDistance(pvt,e\room\obj)*2
				
				;mainPlayer\camShake = 5.0
				;mainPlayer\blurTimer = 100
				
				mainPlayer\blinkTimer = -10
				
				PlaySound_Strict DripSFX(0);HorrorSFX(11)
				
				PositionEntity mainPlayer\collider, EntityX(pvt),EntityY(pvt)+0.05,EntityZ(pvt)
				UpdateWorld()
				;ResetEntity Collider
				
				TurnEntity mainPlayer\collider, 0,180,0
				
				FreeEntity pvt
				RemoveEvent(e)							
			EndIf
		EndIf
	EndIf
	;[End Block]
End Function



Function UpdateEvent1048a(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	
	If e\room\Objects[0]=0 Then
		If mainPlayer\currRoom<>e\room And mainPlayer\blinkTimer<-10 Then
			dist = Distance(EntityX(mainPlayer\collider),EntityZ(mainPlayer\collider), EntityX(e\room\obj),EntityZ(e\room\obj))
			If (dist<16.0) Then
				e\room\Objects[0] =	LoadAnimMesh_Strict("GFX\npcs\scp-1048a.b3d")
				ScaleEntity e\room\Objects[0], 0.05,0.05,0.05
				SetAnimTime(e\room\Objects[0], 2)
				PositionEntity(e\room\Objects[0], EntityX(e\room\obj), 0.0, EntityZ(e\room\obj))
				
				RotateEntity(e\room\Objects[0], -90.0, Rnd(0.0, 360.0), 0.0)
				
				e\Sound = LoadSound_Strict("SFX\SCP\1048A\Shriek.ogg")
				e\Sound2 = LoadSound_Strict("SFX\SCP\1048A\Growth.ogg")
				
				e\EventState = 1
			EndIf
		EndIf
	Else
		e\EventState3 = 1
		Select e\EventState
			Case 1
				Animate2(e\room\Objects[0], AnimTime(e\room\Objects[0]), 2.0, 395.0, 1.0)
				
				If (EntityDistance(mainPlayer\collider, e\room\Objects[0])<2.5) Then e\EventState = 2
			Case 2
				Local prevFrame# = AnimTime(e\room\Objects[0]) 
				Animate2(e\room\Objects[0], prevFrame, 2.0, 647.0, 1.0, False)
				
				If (prevFrame <= 400.0 And AnimTime(e\room\Objects[0])>400.0) Then
					e\SoundCHN = PlaySound_Strict(e\Sound)
				EndIf
				
				Local volume# = Max(1.0 - Abs(prevFrame - 600.0)/100.0, 0.0)
				
				mainPlayer\blurTimer = volume*1000.0
				mainPlayer\camShake = volume*10.0
				
				PointEntity(e\room\Objects[0], mainPlayer\collider)
				RotateEntity(e\room\Objects[0], -90.0, EntityYaw(e\room\Objects[0]), 0.0)
				
				If (prevFrame>646.0) Then
					If (mainPlayer\currRoom = e\room) Then
						e\EventState = 3	
						PlaySound_Strict e\Sound2
						
						Msg = "Something is growing all around your body."
						MsgTimer = 70.0 * 3.0
					Else
						e\EventState3 = 70*30
					EndIf
				EndIf
			Case 3
				e\EventState2 = e\EventState2 + FPSfactor
				
				mainPlayer\blurTimer = e\EventState2*2.0
				
				If (e\EventState2>250.0 And e\EventState2-FPSfactor <= 250.0) Then
					Select Rand(3)
						Case 1
							Msg = "Ears are growing all over your body."
						Case 2
							Msg = "Ear-like organs are growing all over your body."
						Case 3
							Msg = "Ears are growing all over your body. They are crawling on your skin."
					End Select

					MsgTimer = 70.0 * 3.0
				Else If (e\EventState2>600.0 And e\EventState2-FPSfactor <= 600.0)
					Select Rand(4)
						Case 1
							Msg = "It is becoming difficult to breathe."
						Case 2
							Msg = "You have excellent hearing now. Also, you are dying."
						Case 3
							Msg = "The ears are growing inside your body."
						Case 4
							Msg = Chr(34)+"Can't... Breathe..."+Chr(34)
					End Select

					MsgTimer = 70.0 * 5.0
				EndIf
				
				If (e\EventState2>70*15) Then
					Kill(mainPlayer)
					e\EventState = 4
					RemoveEvent(e)
				EndIf
		End Select 
		
		If (e <> Null) Then
			If mainPlayer\currRoom <> e\room Then
				If e\EventState3>0 Then
					e\EventState3 = e\EventState3+FPSfactor
					
					If e\EventState3>70*25 Then
						FreeEntity(e\room\Objects[0])
						e\room\Objects[0]=0
						RemoveEvent(e)
					EndIf
				EndIf
			EndIf
		EndIf
	EndIf
	
	;[End Block]
End Function

