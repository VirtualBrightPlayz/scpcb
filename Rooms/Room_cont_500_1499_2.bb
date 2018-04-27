Function FillRoom_cont_500_1499_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump
    
    r\RoomDoors[0] = CreateDoor(r\zone, r\x + 288.0*RoomScale, r\y, r\z + 576.0*RoomScale, 90, r, False, False, 3)
    r\RoomDoors[0]\open = False : r\RoomDoors[0]\locked = True
    d = CreateDoor(r\zone, r\x + 777.0*RoomScale, r\y, r\z + 671.0*RoomScale, 90, r, False, False)
    d = CreateDoor(r\zone, r\x + 556.0*RoomScale, r\y, r\z + 296.0*RoomScale, 0, r, False, False)
    r\Objects[0] = CreatePivot()
    PositionEntity r\Objects[0],r\x + 576.0*RoomScale,r\y+160.0*RoomScale,r\z+632.0*RoomScale
    EntityParent r\Objects[0],r\obj
    
    it = CreateItem("SCP-1499", "scp1499", r\x + 600.0 * RoomScale, r\y + 176.0 * RoomScale, r\z - 228.0 * RoomScale)
    RotateEntity it\collider, 0, r\angle, 0
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Emily Ross' Badge", "badge", r\x + 364.0 * RoomScale, r\y + 5.0 * RoomScale, r\z + 716.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function


Function UpdateEvent_cont_500_1499_2(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	;If mainPlayer\currRoom = e\room
	If e\room\dist < 15
		If Contained106 Then e\EventState = 2.0
		If Curr106\State < 0 Then e\EventState = 2.0
		
		If e\EventState < 2.0
			If e\EventState = 0.0
				LoadEventSound(e,"SFX\Character\Scientist\EmilyScream.ogg")
				e\soundChannels[0] = PlaySound2(e\sounds[0], mainPlayer\cam, e\room\Objects[0], 100, 1.0)
				de.Decals = CreateDecal(0, EntityX(e\room\Objects[0],True), e\room\y+2.0*RoomScale, EntityZ(e\room\Objects[0],True), 90, Rand(360), 0)
				de\Size = 0.5 : EntityAlpha(de\obj, 0.8)
				EntityFX de\obj,1
				e\EventState = 1.0
			ElseIf e\EventState = 1.0
				If (Not ChannelPlaying(e\soundChannels[0]))
					e\EventState = 2.0
					e\room\RoomDoors[0]\locked = False
				Else
					UpdateSoundOrigin(e\soundChannels[0],mainPlayer\cam,e\room\Objects[0],100,1.0)
				EndIf
			EndIf
		Else
			DebugLog "Removed 'room2scps2' event"
			e\room\RoomDoors[0]\locked = False
			de.Decals = CreateDecal(0, EntityX(e\room\Objects[0],True), e\room\y+2.0*RoomScale, EntityZ(e\room\Objects[0],True), 90, Rand(360), 0)
			de\Size = 0.5 : EntityAlpha(de\obj, 0.8)
			EntityFX de\obj,1
			RemoveEvent(e)
		EndIf
	EndIf
	;[End Block]
End Function

