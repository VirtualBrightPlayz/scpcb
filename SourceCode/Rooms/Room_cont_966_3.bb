Function FillRoom_cont_966_3(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%

	Local t1%;, Bump

    d = CreateDoor(r\zone, r\x - 400.0 * RoomScale, 0, r\z, -90, r, False, False, 3)
    d = CreateDoor(r\zone, r\x, 0, r\z - 480.0 * RoomScale, 180, r, False, False, 3);: d\buttons[0] = False

    ;PositionEntity(d\buttons[0], EntityX(d\buttons[0],True), EntityY(d\buttons[0],True), r\z + 288.0 * RoomScale, True)
    ;PositionEntity(d\buttons[1], EntityX(d\buttons[1],True), EntityY(d\buttons[1],True), r\z + 320.0 * RoomScale, True)

    sc.SecurityCams = CreateSecurityCam(r\x-312.0 * RoomScale, r\y + 414*RoomScale, r\z + 656*RoomScale, r)
    sc\angle = 225
    sc\turn = 45
    TurnEntity(sc\cameraObj, 20, 0, 0)
    ;sc\followPlayer = True
    sc\id = 9

    r\objects[0] = CreatePivot(r\obj)
    PositionEntity(r\objects[0], r\x, 0.5, r\z + 512.0 * RoomScale, True)

    r\objects[1] = CreatePivot(r\obj)
    PositionEntity(r\objects[1], r\x + 64.0 * RoomScale, 0.5, r\z - 640.0 * RoomScale, True)

    r\objects[2] = CreatePivot(r\obj)
    PositionEntity(r\objects[2], r\x - 608.0 * RoomScale, 0.5, r\z, True)

    r\objects[3] = CreatePivot(r\obj)
    PositionEntity(r\objects[3], r\x + 320.0 * RoomScale, 0.5, r\z + 704.0 * RoomScale, True)

    it = CreateItem("nvgoggles", r\x + 320.0 * RoomScale, 0.5, r\z + 704.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function


Function UpdateEvent_cont_966_3(e.Events)
	If (mainPlayer\currRoom = e\room) Then
		If ((Not e\loaded)) Then
			CreateNPC(NPCtype966, EntityX(e\room\objects[0],True), EntityY(e\room\objects[0],True), EntityZ(e\room\objects[0],True))
			CreateNPC(NPCtype966, EntityX(e\room\objects[1],True), EntityY(e\room\objects[1],True), EntityZ(e\room\objects[1],True))
			CreateNPC(NPCtype966, EntityX(e\room\objects[2],True), EntityY(e\room\objects[2],True), EntityZ(e\room\objects[2],True))

			e\loaded = True
		EndIf
		RemoveEvent(e)
	EndIf
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D