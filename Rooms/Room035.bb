Function FillRoom035(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    d = CreateDoor(r\zone, r\x - 296.0 * RoomScale, 0, r\z - 672.0 * RoomScale, 180, r, True, 0, 5)
    d\AutoClose = False : d\locked = True : r\RoomDoors[0]=d
    PositionEntity (d\buttons[1], r\x - 164.0 * RoomScale, EntityY(d\buttons[1],True), EntityZ(d\buttons[1],True), True)
    FreeEntity d\buttons[0] : d\buttons[0]=0
    FreeEntity d\obj2 : d\obj2=0
    
    d2 = CreateDoor(r\zone, r\x - 296.0 * RoomScale, 0, r\z - 144.0 * RoomScale, 0, r, False)
    d2\AutoClose = False : d2\locked = True : r\RoomDoors[1]=d2
    PositionEntity (d2\buttons[0], r\x - 432.0 * RoomScale, EntityY(d2\buttons[0],True), r\z - 480.0 * RoomScale, True)
    RotateEntity(d2\buttons[0], 0, 90, 0, True)
    FreeEntity d2\buttons[1] : d2\buttons[1]=0
    FreeEntity d2\obj2 : d2\obj2=0
    
    ;door to the control room
    r\RoomDoors[2] = CreateDoor(r\zone, r\x + 384.0 * RoomScale, 0, r\z - 672.0 * RoomScale, 180, r, False, 0, 5)
    r\RoomDoors[2]\AutoClose = False
    
    ;door to the storage room
    r\RoomDoors[3] = CreateDoor(0, r\x + 768.0 * RoomScale, 0, r\z +512.0 * RoomScale, 90, r, False, 0, 0, "5731")
    r\RoomDoors[3]\AutoClose = False			
    
    d\LinkedDoor = d2 : d2\LinkedDoor = d
    
    For i = 0 To 1
        r\Objects[i*2] = CopyEntity(LeverBaseOBJ)
        r\Objects[i*2+1] = CopyEntity(LeverOBJ)
        
        r\Levers[i] = r\Objects[i*2+1]
        
        For n% = 0 To 1
            ScaleEntity(r\Objects[i*2+n], 0.04, 0.04, 0.04)
            PositionEntity (r\Objects[i*2+n], r\x + 210.0 * RoomScale, r\y + 224.0 * RoomScale, r\z - (208-i*76) * RoomScale, True)
            
            EntityParent(r\Objects[i*2+n], r\obj)
        Next
        
        RotateEntity(r\Objects[i*2], 0, -90-180, 0)
        RotateEntity(r\Objects[i*2+1], -80, -90, 0)
        
        EntityPickMode r\Objects[i*2+1], 1, False
        EntityRadius r\Objects[i*2+1], 0.1				
    Next
    
    ;the control room
    r\Objects[3] = CreatePivot(r\obj)
    PositionEntity(r\Objects[3], r\x + 456 * RoomScale, 0.5, r\z + 400.0 * RoomScale, True)
    
    r\Objects[4] = CreatePivot(r\obj)
    PositionEntity(r\Objects[4], r\x - 576 * RoomScale, 0.5, r\z + 640.0 * RoomScale, True)
    
    For i = 0 To 1
        em.Emitters = CreateEmitter(r\x - 272.0 * RoomScale, 10, r\z + (624.0-i*512) * RoomScale, 0)
        TurnEntity(em\Obj, 90, 0, 0, True)
        EntityParent(em\Obj, r\obj)
        em\RandAngle = 15
        em\Speed = 0.05
        em\SizeChange = 0.007
        em\Achange = -0.006
        em\Gravity = -0.24
        
        r\Objects[5+i]=em\Obj
    Next
    
    ;the corners of the cont chamber (needed to calculate whether the player is inside the chamber)
    r\Objects[7] = CreatePivot(r\obj)
    PositionEntity(r\Objects[7], r\x - 720 * RoomScale, 0.5, r\z + 880.0 * RoomScale, True)
    r\Objects[8] = CreatePivot(r\obj)
    PositionEntity(r\Objects[8], r\x + 176 * RoomScale, 0.5, r\z - 144.0 * RoomScale, True)			
    
    it = CreateItem("SCP-035 Addendum", "paper", r\x + 248.0 * RoomScale, r\y + 220.0 * RoomScale, r\z + 576.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Radio Transceiver", "radio", r\x - 544.0 * RoomScale, 0.5, r\z + 704.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("SCP-500-01", "scp500", r\x + 1168*RoomScale, 224*RoomScale, r\z+576*RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Metal Panel", "scp148", r\x - 360 * RoomScale, 0.5, r\z + 644 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Document SCP-035", "paper", r\x + 1168.0 * RoomScale, 104.0 * RoomScale, r\z + 608.0 * RoomScale)
    EntityParent(it\collider, r\obj)
End Function
