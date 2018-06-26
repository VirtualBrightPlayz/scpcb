Function FillRoom_off_glss_3(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
    Local it.Item, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    d = CreateDoor(r\x + 736.0 * RoomScale, 0.0, r\z + 240.0 * RoomScale, 0, r, False, DOOR_TYPE_DEF, r\roomTemplate\name)
    PositionEntity(d\buttons[0], r\x + 892.0 * RoomScale, EntityY(d\buttons[0],True), r\z + 224.0 * RoomScale, True)
    PositionEntity(d\buttons[1], r\x + 892.0 * RoomScale, EntityY(d\buttons[1],True), r\z + 255.0 * RoomScale, True)
    FreeEntity(d\obj2)
	d\obj2 = 0

    ;r\objects[0] = LoadMesh("GFX/Map/room3offices_hb.b3d",r\obj)
    ;EntityPickMode(r\objects[0],2)
    ;EntityType(r\objects[0],HIT_MAP)
    ;EntityAlpha(r\objects[0],0.0)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D