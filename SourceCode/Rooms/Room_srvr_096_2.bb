Function FillRoom_srvr_096_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    d.Doors = CreateDoor(0, r\x,0,r\z, 0, r, False, 2, False)
    d\locked = True

    r\roomDoors[0] = CreateDoor(r\zone, r\x - 208.0 * RoomScale, 0.0, r\z - 736.0 * RoomScale, 90, r, True, False)
    r\roomDoors[0]\autoClose=False
    r\roomDoors[1] = CreateDoor(r\zone, r\x - 208.0 * RoomScale, 0.0, r\z + 736.0 * RoomScale, 90, r, True, False)
    r\roomDoors[1]\autoClose=False

    r\roomDoors[2] = CreateDoor(r\zone, r\x - 672.0 * RoomScale, 0.0, r\z - 1024.0 * RoomScale, 0, r, False, False, False, "GEAR")
    r\roomDoors[2]\autoClose=False
    FreeEntity(r\roomDoors[2]\buttons[0]) : r\roomDoors[2]\buttons[0]=0
    FreeEntity(r\roomDoors[2]\buttons[1]) : r\roomDoors[2]\buttons[1]=0

	Local n%
    For n% = 0 To 2
		r\levers[n] = CreateLever()

		ScaleEntity(r\levers[n]\obj, 0.03, 0.03, 0.03)
		ScaleEntity(r\levers[n]\baseObj, 0.03, 0.03, 0.03)

		Select n
			Case 0 ;power switch
				PositionEntity(r\levers[n]\obj, r\x - 1260.0 * RoomScale, r\y + 234.0 * RoomScale, r\z + 750 * RoomScale, True)
				PositionEntity(r\levers[n]\baseObj, r\x - 1260.0 * RoomScale, r\y + 234.0 * RoomScale, r\z + 750 * RoomScale, True)
			Case 1 ;generator fuel pump
				PositionEntity(r\levers[n]\obj, r\x - 920.0 * RoomScale, r\y + 164.0 * RoomScale, r\z + 898 * RoomScale, True)
				PositionEntity(r\levers[n]\baseObj, r\x - 920.0 * RoomScale, r\y + 164.0 * RoomScale, r\z + 898 * RoomScale, True)
			Case 2 ;generator on/off
				PositionEntity(r\levers[n]\obj, r\x - 837.0 * RoomScale, r\y + 152.0 * RoomScale, r\z + 886 * RoomScale, True)
				PositionEntity(r\levers[n]\baseObj, r\x - 837.0 * RoomScale, r\y + 152.0 * RoomScale, r\z + 886 * RoomScale, True)
		End Select

		EntityParent(r\levers[n]\obj, r\obj)
		EntityParent(r\levers[n]\baseObj, r\obj)

        ;RotateEntity(r\objects[n * 2], 0, -90, 0)
        RotateEntity(r\levers[n]\obj, 81, -180, 0)

        ;EntityPickMode(r\levers[n]\obj, 2)
        EntityPickMode(r\levers[n]\obj, 1, False)
        EntityRadius(r\levers[n]\obj, 0.1)
        ;makecollbox(r\levers[n]\obj)
    Next

    RotateEntity(r\levers[1]\obj, -81, -180, 0)
    RotateEntity(r\levers[2]\obj, -81, -180, 0)

    ;096 spawnpoint
    r\objects[6]=CreatePivot(r\obj)
    PositionEntity(r\objects[6], r\x - 848*RoomScale, 0.5, r\z-576*RoomScale, True)
    ;guard spawnpoint
    r\objects[7]=CreatePivot(r\obj)
    PositionEntity(r\objects[7], r\x - 1328.0 * RoomScale, 0.5, r\z + 528*RoomScale, True)
    ;the point where the guard walks to
    r\objects[8]=CreatePivot(r\obj)
    PositionEntity(r\objects[8], r\x - 1376.0 * RoomScale, 0.5, r\z + 32*RoomScale, True)

    r\objects[9]=CreatePivot(r\obj)
    PositionEntity(r\objects[9], r\x - 848*RoomScale, 0.5, r\z+576*RoomScale, True)
End Function


Function UpdateEvent_srvr_096_2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams, de.Decals

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If (e\eventState=0) Then
		If (mainPlayer\currRoom = e\room) Then
		 ;close the doors when the player enters the room
			UseDoor(e\room\roomDoors[0],False)
			e\room\roomDoors[0]\locked = True
			UseDoor(e\room\roomDoors[1],False)
			e\room\roomDoors[1]\locked = True

			If (Curr096=Null) Then
				Curr096 = CreateNPC(NPCtype096, EntityX(e\room\objects[6],True),EntityY(e\room\objects[6],True)+0.1,EntityZ(e\room\objects[6],True))
			Else
				PositionEntity(Curr096\collider, EntityX(e\room\objects[6],True),EntityY(e\room\objects[6],True)+0.1,EntityZ(e\room\objects[6],True),True)
			EndIf

			RotateEntity(Curr096\collider, 0, e\room\angle, 0, True)
			ResetEntity(Curr096\collider)
			Curr096\state=2
			Curr096\state2=70*10

			LoadEventSound(e,"SFX/Character/Guard/096ServerRoom1.ogg")
			e\soundChannels[0] = PlaySound(e\sounds[0])

			e\room\npc[0]=CreateNPC(NPCtypeGuard, EntityX(e\room\objects[7],True),EntityY(e\room\objects[7],True),EntityZ(e\room\objects[7],True))

			e\eventState=1
		EndIf
	ElseIf (e\eventState < 70*45) Then
		If (Rand(200)<5 And mainPlayer\currRoom = e\room) Then
			;LightBlink = Rnd(1.0,2.0)
			If (Rand(5)=1) Then PlayRangedSound(IntroSFX(Rand(10,12)), mainPlayer\cam, e\room\obj, 8.0, Rnd(0.1,0.3))
		EndIf

		e\eventState=Min(e\eventState+timing\tickDuration,70*43)

		If (e\room\npc[0]<>Null) Then
			If (e\eventState < 70*13) Then

				If (e\eventState > 70*8) Then
					Curr096\state=Min(Max(2,Curr096\state),3)
					Curr096\state2=70*10
				Else
					Curr096\state = 5
					PointEntity(Curr096\collider, e\room\objects[9])
					RotateEntity(Curr096\collider, 0, EntityYaw(Curr096\collider,True),0,True)
					If (EntityDistance(Curr096\collider, e\room\objects[8])<2.4) Then
						Curr096\state2 = 0
					Else
						Curr096\state2 = 1001
					EndIf
				EndIf

				e\room\npc[0]\state=8
				SetAnimTime(e\room\npc[0]\obj, 115)
				PointEntity(e\room\npc[0]\collider, Curr096\collider)
			ElseIf (e\eventState-timing\tickDuration =< 70*15) Then ;walk to the doorway
				If (e\eventState > 70*15) Then
					e\room\npc[0]\state=3
					e\room\npc[0]\pathStatus = FindPath(e\room\npc[0], EntityX(e\room\objects[8],True),0.5,EntityZ(e\room\objects[8],True))
					e\room\npc[0]\pathTimer=300
				EndIf
			ElseIf (e\eventState<70*20) Then
				If (e\room\npc[0]\pathStatus=0) Then
					e\room\roomDoors[2]\open = False

					e\room\npc[0]\state=7
					;SetAnimTime(e\room\npc[0]\obj, 115)
					PointEntity(e\room\npc[0]\obj, Curr096\collider)
					RotateEntity(e\room\npc[0]\collider, 0, CurveAngle(EntityYaw(e\room\npc[0]\obj),EntityYaw(e\room\npc[0]\collider),30),0)

				EndIf
			Else ;start walking away

				If (Curr096\state = 4) Then ;shoot at 096 when it starts attacking
					Curr096\lastSeen=1

					e\room\npc[0]\state = 2
					PointEntity(e\room\npc[0]\obj, Curr096\collider)
					RotateEntity(e\room\npc[0]\collider, 0, CurveAngle(EntityYaw(e\room\npc[0]\obj),EntityYaw(e\room\npc[0]\collider),30),0)
					;If (mainPlayer\currRoom = e\room) Then LightBlink = (e\room\npc[0]\reload)+Rnd(0.5,2.0)
					Curr096\target = e\room\npc[0]
				Else
					If (e\eventState>70*22) Then Curr096\state = 4
					If (e\room\npc[0]\state=7) Then
						e\room\npc[0]\state=3
						e\room\npc[0]\pathStatus = FindPath(e\room\npc[0], EntityX(e\room\obj,True),0.4,EntityZ(e\room\obj,True))
						e\room\npc[0]\pathTimer=300
					EndIf
				EndIf
			EndIf

			Curr096\target = e\room\npc[0]
			If (AnimTime(Curr096\obj)>25 And AnimTime(Curr096\obj)<150) Then
				FreeSound(e\sounds[0])
				e\sounds[0] = 0
				e\sounds[0]=LoadSound("SFX/Character/Guard/096ServerRoom2.ogg")
				e\soundChannels[0]=PlaySound(e\sounds[0])

				Curr096\currSpeed = 0

				For i = 0 To 6
					If (e\room\angle = 0 Or e\room\angle = 180) Then
						de.Decals = CreateDecal(Rand(2,3), e\room\x-Rnd(197,199)*Cos(e\room\angle)*RoomScale, 1.0, e\room\z+(140.0*(i-3))*RoomScale,0,e\room\angle+90,Rnd(360))
						de\size = Rnd(0.8,0.85) : de\sizeChange = 0.001
						de.Decals = CreateDecal(Rand(2,3), e\room\x-Rnd(197,199)*Cos(e\room\angle)*RoomScale, 1.0, e\room\z+(140.0*(i-3))*RoomScale,0,e\room\angle-90,Rnd(360))
						de\size = Rnd(0.8,0.85) : de\sizeChange = 0.001
					Else
						de.Decals = CreateDecal(Rand(2,3), e\room\x+(140.0*(i-3))*RoomScale, 1.0, e\room\z-Rnd(197,199)*Sin(e\room\angle)*RoomScale-Rnd(0.001,0.003),0,e\room\angle+90,Rnd(360))
						de\size = Rnd(0.8,0.85) : de\sizeChange = 0.001
						de.Decals = CreateDecal(Rand(2,3), e\room\x+(140.0*(i-3))*RoomScale, 1.0, e\room\z-Rnd(197,199)*Sin(e\room\angle)*RoomScale-Rnd(0.001,0.003),0,e\room\angle-90,Rnd(360))
						de\size = Rnd(0.8,0.85) : de\sizeChange = 0.001
					EndIf
					de.Decals = CreateDecal(Rand(2,3), EntityX(e\room\npc[0]\collider)+Rnd(-2,2),Rnd(0.001,0.003),EntityZ(e\room\npc[0]\collider)+Rnd(-2,2),90,Rnd(360),0)

				Next
				de\size = Rnd(0.5,0.7)
				ScaleSprite(de\obj, de\size,de\size)

				Curr096\state=5

				RemoveNPC(e\room\npc[0])
				e\room\npc[0]=Null

			EndIf
		Else

			If (e\eventState >= 70*40 And e\eventState-timing\tickDuration < 70*40) Then ;open them again to let the player in
				e\room\roomDoors[0]\locked=False
				e\room\roomDoors[1]\locked=False
				UseDoor(e\room\roomDoors[0],False)
				UseDoor(e\room\roomDoors[1],False)
				FreeSound(e\sounds[0])
				e\sounds[0] = 0
				e\room\roomDoors[0]\locked=True
				e\room\roomDoors[1]\locked=True
			EndIf

			If (mainPlayer\currRoom = e\room) Then
				If (e\soundChannels[0]<>0) Then
					If (IsChannelPlaying(e\soundChannels[0])) Then
						;LightBlink = Rnd(0.5,6.0)
						If (Rand(50)=1) Then PlayRangedSound(IntroSFX(Rand(10,12)), mainPlayer\cam, e\room\obj, 8.0, Rnd(0.1,0.3))
					EndIf
				EndIf

				If ((e\room\angle = 0 Or e\room\angle = 180)) Then ;lock the player inside
					If (Abs(EntityX(mainPlayer\collider)-EntityX(e\room\obj,True))> 1.3) Then
						e\eventState = 70*50
						e\sounds[0]=0
					EndIf
				Else
					If (Abs(EntityZ(mainPlayer\collider)-EntityZ(e\room\obj,True))> 1.3) Then
						e\eventState = 70*50
						e\sounds[0]=0
					EndIf
				EndIf
			EndIf

		EndIf

	ElseIf (mainPlayer\currRoom = e\room) Then
		temp = e\room\levers[0]\succ ;power switch
		x = e\room\levers[1]\succ ;fuel pump
		z = e\room\levers[2]\succ ;generator

		;fuel pump on
		If (x) Then
			e\eventState2 = Min(1.0, e\eventState2+timing\tickDuration/350)

			;generator on
			If (z) Then
				If (e\sounds[1]=0) Then LoadEventSound(e,"SFX/General/GeneratorOn.ogg",1)
				e\eventState3 = Min(1.0, e\eventState3+timing\tickDuration/450)
			Else
				e\eventState3 = Min(0.0, e\eventState3-timing\tickDuration/450)
			EndIf
		Else
			e\eventState2 = Max(0, e\eventState2-timing\tickDuration/350)
			e\eventState3 = Max(0, e\eventState3-timing\tickDuration/450)
		EndIf

		If (e\eventState2>0) Then e\soundChannels[0]=LoopRangedSound(RoomAmbience[8], e\soundChannels[0], mainPlayer\cam, e\room\levers[1]\obj, 5.0, e\eventState2*0.8)
		If (e\eventState3>0) Then e\soundChannels[1]=LoopRangedSound(e\sounds[1], e\soundChannels[1], mainPlayer\cam, e\room\levers[2]\obj, 6.0, e\eventState3)

		If (temp=0 And x And z) Then
			e\room\roomDoors[0]\locked = False
			e\room\roomDoors[1]\locked = False
		Else
			;If (Rand(200)<5) Then LightBlink = Rnd(0.5,1.0)

			If (e\room\roomDoors[0]\open) Then
				e\room\roomDoors[0]\locked = False
				UseDoor(e\room\roomDoors[0],False)
			EndIf
			If (e\room\roomDoors[1]\open) Then
				e\room\roomDoors[1]\locked = False
				UseDoor(e\room\roomDoors[1],False)
			EndIf
			e\room\roomDoors[0]\locked=True
			e\room\roomDoors[1]\locked=True
		EndIf
	EndIf

	;[End Block]
End Function


;~IDEal Editor Parameters:
;~C#Blitz3D