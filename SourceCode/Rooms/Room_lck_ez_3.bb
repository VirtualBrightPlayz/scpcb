Function FillRoom_lck_ez_3(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    d = CreateDoor(r\zone, r\x - 728.0 * RoomScale, 0.0, r\z - 458.0 * RoomScale, 0, r, False, False, 3)
    d\autoClose = False	: d\open = False  : d\locked = False

    d = CreateDoor(r\zone, r\x - 223.0 * RoomScale, 0.0, r\z - 736.0 * RoomScale, -90, r, False, False, 3)
    d\autoClose = False	: d\open = False  : d\locked = False

    r\roomDoors[0] = CreateDoor(r\zone, r\x - 459.0 * RoomScale, 0.0, r\z + 339.0 * RoomScale, 90, r, False, False)
    PositionEntity(r\roomDoors[0]\buttons[0], r\x + 580.822 * RoomScale, EntityY(r\roomDoors[0]\buttons[0],True), r\z - 606.679 * RoomScale, True)
    PositionEntity(r\roomDoors[0]\buttons[1], r\x + 580.822 * RoomScale, EntityY(r\roomDoors[0]\buttons[1],True), r\z - 606.679 * RoomScale, True)
    r\roomDoors[0]\dir = 0 : r\roomDoors[0]\autoClose = False	: r\roomDoors[0]\open = True  : r\roomDoors[0]\locked = True
    r\roomDoors[0]\mtfClose = False

    r\roomDoors[1] = CreateDoor(r\zone, r\x + 385.0 * RoomScale, 0.0, r\z + 339.0 * RoomScale, 270, r, False, False)
    PositionEntity(r\roomDoors[1]\buttons[0], r\x + 580.822 * RoomScale, EntityY(r\roomDoors[1]\buttons[0],True), r\z - 606.679 * RoomScale, True)
    PositionEntity(r\roomDoors[1]\buttons[1], r\x + 580.822 * RoomScale, EntityY(r\roomDoors[1]\buttons[1],True), r\z - 606.679 * RoomScale, True)
    r\roomDoors[1]\dir = 0 : r\roomDoors[1]\autoClose = False	: r\roomDoors[1]\open = True  : r\roomDoors[1]\locked = True
    r\roomDoors[1]\mtfClose = False
    FreeEntity r\roomDoors[1]\obj2 : r\roomDoors[1]\obj2 = 0

    r\objects[0] = CreatePivot()
    PositionEntity r\objects[0],r\x-48.0*RoomScale,128.0*RoomScale,r\z+320.0*RoomScale
    EntityParent r\objects[0],r\obj

    For r2.Rooms = Each Rooms
        If r2<>r Then
            If r2\roomTemplate\name = "room3gw" Then
                r\objects[3] = CopyEntity(r2\objects[3],r\obj) ;don't load the mesh again
                Exit
            EndIf
        EndIf
    Next
    If r\objects[3]=0 Then r\objects[3] = LoadMesh("GFX/Map/room3gw_pipes.b3d",r\obj)
    EntityPickMode r\objects[3],2
End Function
