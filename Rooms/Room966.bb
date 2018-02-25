Function FillRoom966(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    d = CreateDoor(r\zone, r\x - 400.0 * RoomScale, 0, r\z, -90, r, False, False, 3)
    d = CreateDoor(r\zone, r\x, 0, r\z - 480.0 * RoomScale, 180, r, False, False, 3);: d\buttons[0] = False

    ;PositionEntity (d\buttons[0], EntityX(d\buttons[0],True), EntityY(d\buttons[0],True), r\z + 288.0 * RoomScale, True)
    ;PositionEntity (d\buttons[1], EntityX(d\buttons[1],True), EntityY(d\buttons[1],True), r\z + 320.0 * RoomScale, True)
    
    sc.SecurityCams = CreateSecurityCam(r\x-312.0 * RoomScale, r\y + 414*RoomScale, r\z + 656*RoomScale, r)
    sc\angle = 225
    sc\turn = 45
    TurnEntity(sc\CameraObj, 20, 0, 0)
    ;sc\FollowPlayer = True
    sc\ID = 9
    
    r\Objects[0] = CreatePivot(r\obj)
    PositionEntity(r\Objects[0], r\x, 0.5, r\z + 512.0 * RoomScale, True)
    
    r\Objects[1] = CreatePivot(r\obj)
    PositionEntity(r\Objects[1], r\x + 64.0 * RoomScale, 0.5, r\z - 640.0 * RoomScale, True)
    
    r\Objects[2] = CreatePivot(r\obj)
    PositionEntity(r\Objects[2], r\x - 608.0 * RoomScale, 0.5, r\z, True)
    
    r\Objects[3] = CreatePivot(r\obj)
    PositionEntity(r\Objects[3], r\x + 320.0 * RoomScale, 0.5, r\z + 704.0 * RoomScale, True)
    
    it = CreateItem("Night Vision Goggles", "nvgoggles", r\x + 320.0 * RoomScale, 0.5, r\z + 704.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function


Function UpdateEventRoom966(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		Select e\EventState
			Case 0
				;a dirty workaround to hide the pause when loading 966 model
				LightBlink = 5.0
				e\EventState = 1
				PlaySound_Strict LightSFX
				QuickLoadPercent = 0
			Case 1
				e\EventState2 = e\EventState2+FPSfactor
				If e\EventState2>30 Then
					If e\EventStr = ""
						CreateNPC(NPCtype966, EntityX(e\room\Objects[0],True), EntityY(e\room\Objects[0],True), EntityZ(e\room\Objects[0],True))
						QuickLoadPercent = 50
						e\EventStr = "load0"
					ElseIf e\EventStr = "load0"
						CreateNPC(NPCtype966, EntityX(e\room\Objects[1],True), EntityY(e\room\Objects[1],True), EntityZ(e\room\Objects[1],True))
						QuickLoadPercent = 70
						e\EventStr = "load1"
					ElseIf e\EventStr = "load1"
						CreateNPC(NPCtype966, EntityX(e\room\Objects[2],True), EntityY(e\room\Objects[2],True), EntityZ(e\room\Objects[2],True))
						QuickLoadPercent = 100
						e\EventState=2
					EndIf
				Else
					QuickLoadPercent = Int(e\EventState2)
				EndIf							
			Case 2
				
				e\EventState = 2
				RemoveEvent (e)
		End Select
	EndIf
	;[End Block]
End Function

