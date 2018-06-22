Function FillRoom_off_glss_3(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    d.Doors = CreateDoor(r\zone, r\x + 736.0 * RoomScale, 0.0, r\z + 240.0 * RoomScale, 0, r, False, False, 3)
    PositionEntity(d\buttons[0], r\x + 892.0 * RoomScale, EntityY(d\buttons[0],True), r\z + 224.0 * RoomScale, True)
    PositionEntity(d\buttons[1], r\x + 892.0 * RoomScale, EntityY(d\buttons[1],True), r\z + 255.0 * RoomScale, True)
    FreeEntity d\obj2 : d\obj2 = 0

    ;r\objects[0] = LoadMesh("GFX/Map/room3offices_hb.b3d",r\obj)
    ;EntityPickMode r\objects[0],2
    ;EntityType r\objects[0],HIT_MAP
    ;EntityAlpha r\objects[0],0.0
End Function
