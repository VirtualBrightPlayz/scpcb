Function FillRoomCheckpoint1(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    r\RoomDoors[0] = CreateDoor(0, r\x + 48.0*RoomScale, 0, r\z - 128.0 * RoomScale, 0, r, False, False, 3)
    PositionEntity(r\RoomDoors[0]\buttons[0], r\x - 152.0 * RoomScale, EntityY(r\RoomDoors[0]\buttons[0],True), r\z - 352.0 * RoomScale, True)
    PositionEntity(r\RoomDoors[0]\buttons[1], r\x - 152.0 * RoomScale, EntityY(r\RoomDoors[0]\buttons[1],True), r\z + 96.0 * RoomScale, True)
    
    r\RoomDoors[1] = CreateDoor(0, r\x - 352.0*RoomScale, 0, r\z - 128.0 * RoomScale, 0, r, False, False, 3)
    ;FreeEntity r\RoomDoors[1]\buttons[0]
    ;FreeEntity r\RoomDoors[1]\buttons[1]
    
    r\RoomDoors[1]\LinkedDoor = r\RoomDoors[0]
    r\RoomDoors[0]\LinkedDoor = r\RoomDoors[1]
    
    r\Objects[0] = CreatePivot(r\obj)
    PositionEntity (r\Objects[0], r\x + 720.0*RoomScale, 120.0*RoomScale, r\z+333.0*RoomScale, True)
    
    r\RoomDoors[0]\timer = 70 * 5
    r\RoomDoors[1]\timer = 70 * 5
    
    sc.SecurityCams = CreateSecurityCam(r\x+192.0*RoomScale, r\y+704.0*RoomScale, r\z-960.0*RoomScale, r)
    sc\angle = 45
    sc\turn = 0
    TurnEntity(sc\CameraObj, 20, 0, 0)
    sc\ID = 5
    
    r\Objects[2] = CopyEntity(Monitor2,r\obj)
    ScaleEntity(r\Objects[2], 2.0, 2.0, 2.0)
    PositionEntity (r\Objects[2], r\x - 152.0*RoomScale, 384.0*RoomScale, r\z+124.0*RoomScale, True)
    RotateEntity (r\Objects[2],0,180,0)
    EntityFX r\Objects[2],1
    
    r\Objects[3] = CopyEntity(Monitor2,r\obj)
    ScaleEntity(r\Objects[3], 2.0, 2.0, 2.0)
    PositionEntity (r\Objects[3], r\x - 152.0*RoomScale, 384.0*RoomScale, r\z-380.0*RoomScale, True)
    RotateEntity (r\Objects[3],0,0,0)
    EntityFX r\Objects[3],1
    
    If MapTemp(Floor(r\x / 8.0),Floor(r\z /8.0)-1)=0 Then
        CreateDoor(r\zone, r\x, 0, r\z  - 4.0, 0, r, 0, False, 0, "GEAR")
    EndIf
End Function
