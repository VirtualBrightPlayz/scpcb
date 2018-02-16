Function FillRoomGateAEntrance(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump

    ;alempi hissi
    r\RoomDoors[0] = CreateDoor(0, r\x+744.0*RoomScale, 0, r\z+512.0*RoomScale, 90, r, True)
    r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = True
    PositionEntity(r\RoomDoors[0]\buttons[1],r\x+688*RoomScale, EntityY(r\RoomDoors[0]\buttons[1],True), r\z+368*RoomScale, True)
    PositionEntity(r\RoomDoors[0]\buttons[0],r\x+784*RoomScale, EntityY(r\RoomDoors[0]\buttons[0],True), r\z+656*RoomScale, True)
    r\Objects[0] = CreatePivot()
    PositionEntity(r\Objects[0], r\x+1048.0*RoomScale, 0, r\z+512.0*RoomScale, True)
    EntityParent r\Objects[0], r\obj
    
    r\RoomDoors[1] = CreateDoor(r\zone, r\x, 0, r\z - 360.0 * RoomScale, 0, r, False, True, 5)
    r\RoomDoors[1]\dir = 1 : r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\open = False
    PositionEntity(r\RoomDoors[1]\buttons[1], r\x+416*RoomScale, EntityY(r\RoomDoors[0]\buttons[1],True), r\z-576*RoomScale, True)
    RotateEntity r\RoomDoors[1]\buttons[1],0,r\angle-90,0,True
    PositionEntity(r\RoomDoors[1]\buttons[0], r\x, 20.0, r\z, True)
End Function