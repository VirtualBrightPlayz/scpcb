Function FillRoom_lck_ez_3(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
    Local it.Item, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    d = CreateDoor(r\x - 728.0 * RoomScale, 0.0, r\z - 458.0 * RoomScale, 0, r, False, DOOR_TYPE_DEF, r\roomTemplate\name

    d = CreateDoor(r\x - 223.0 * RoomScale, 0.0, r\z - 736.0 * RoomScale, -90, r, False, DOOR_TYPE_DEF, r\roomTemplate\name)

    r\doors[0] = CreateDoor(r\x - 459.0 * RoomScale, 0.0, r\z + 339.0 * RoomScale, 90, r)
    PositionEntity(r\doors[0]\buttons[0], r\x + 580.822 * RoomScale, EntityY(r\doors[0]\buttons[0],True), r\z - 606.679 * RoomScale, True)
    PositionEntity(r\doors[0]\buttons[1], r\x + 580.822 * RoomScale, EntityY(r\doors[0]\buttons[1],True), r\z - 606.679 * RoomScale, True)
    r\doors[0]\locked = True
    r\doors[0]\mtfClose = False

    r\doors[1] = CreateDoor(r\x + 385.0 * RoomScale, 0.0, r\z + 339.0 * RoomScale, 270, r)
    PositionEntity(r\doors[1]\buttons[0], r\x + 580.822 * RoomScale, EntityY(r\doors[1]\buttons[0],True), r\z - 606.679 * RoomScale, True)
    PositionEntity(r\doors[1]\buttons[1], r\x + 580.822 * RoomScale, EntityY(r\doors[1]\buttons[1],True), r\z - 606.679 * RoomScale, True)
    r\doors[1]\locked = True
    r\doors[1]\mtfClose = False
    FreeEntity(r\doors[1]\obj2)
	r\doors[1]\obj2 = 0

    r\objects[0] = CreatePivot()
    PositionEntity(r\objects[0],r\x-48.0*RoomScale,128.0*RoomScale,r\z+320.0*RoomScale)
    EntityParent(r\objects[0],r\obj)

    For r2 = Each Room
        If (r2<>r) Then
            If (r2\roomTemplate\name = "room3gw") Then
                r\objects[3] = CopyEntity(r2\objects[3],r\obj) ;don't load the mesh again
                Exit
            EndIf
        EndIf
    Next
    If (r\objects[3]=0) Then r\objects[3] = LoadMesh("GFX/Map/room3gw_pipes.b3d",r\obj)
    EntityPickMode(r\objects[3],2)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D