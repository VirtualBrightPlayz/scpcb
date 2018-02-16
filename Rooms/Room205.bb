Function FillRoom205(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump
    
    d.Doors = CreateDoor(r\zone, r\x + 128.0 * RoomScale, 0, r\z + 640.0 *RoomScale, 90, r, True, False, 3)
    d\AutoClose = False : d\open = False
    ;PositionEntity(d\buttons[0], r\x + 320.0 * RoomScale, EntityY(d\buttons[0],True), EntityZ(d\buttons[0],True), True)
    ;PositionEntity(d\buttons[1], r\x + 224.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)
    
    r\RoomDoors[0] = CreateDoor(r\zone, r\x - 1392.0 * RoomScale, -128.0 * RoomScale, r\z - 384*RoomScale, 0, r, True, False, 3)
    r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = False
    FreeEntity(r\RoomDoors[0]\buttons[0]) : r\RoomDoors[0]\buttons[0]=0
    FreeEntity(r\RoomDoors[0]\buttons[1]) : r\RoomDoors[0]\buttons[1]=0
    
    sc.SecurityCams = CreateSecurityCam(r\x - 1152.0 * RoomScale, r\y + 900.0 * RoomScale, r\z + 176.0 * RoomScale, r, True)
    sc\angle = 90 : sc\turn = 0
    EntityParent(sc\obj, r\obj)
    
    sc\AllowSaving = False
    sc\RenderInterval = 0
    
    EntityParent(sc\ScrObj, 0)
    PositionEntity(sc\ScrObj, r\x - 1716.0 * RoomScale, r\y + 160.0 * RoomScale, r\z + 176.0 * RoomScale, True)
    TurnEntity sc\ScrObj, 0, 90, 0
    ScaleSprite sc\ScrObj, 896.0*0.5*RoomScale, 896.0*0.5*RoomScale
    
    EntityParent(sc\ScrObj, r\obj)
    ;EntityBlend(sc\ScrObj, 2)
    
    CameraZoom (sc\Cam, 1.5)
    
    HideEntity sc\ScrOverlay
    HideEntity sc\MonitorObj
    
    r\Objects[0] = CreatePivot(r\obj)
    PositionEntity r\Objects[0], r\x - 1536.0 * RoomScale, r\y + 730.0 * RoomScale, r\z + 192.0 * RoomScale, True
    RotateEntity r\Objects[0], 0,-90,0,True
    
    r\Objects[1] = sc\ScrObj
End Function
