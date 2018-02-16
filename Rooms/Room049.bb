Function FillRoom049(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    r\Objects[0] = CreatePivot(r\obj)
    PositionEntity(r\Objects[0], r\x + 640.0 * RoomScale, 240.0 * RoomScale, r\z + 656.0 * RoomScale, True)
    
    r\Objects[1] = CreatePivot(r\obj)
    PositionEntity(r\Objects[1], r\x - 2032.0 * RoomScale, -3280.0 * RoomScale, r\z - 656.0 * RoomScale, True)
    
    r\Objects[2] = CreatePivot(r\obj)
    PositionEntity(r\Objects[2], r\x - 640.0 * RoomScale, 240.0 * RoomScale, r\z - 656.0 * RoomScale, True)
    
    r\Objects[3] = CreatePivot(r\obj)
    PositionEntity(r\Objects[3], r\x + 2040.0 * RoomScale, -3280.0 * RoomScale, r\z + 656.0 * RoomScale, True)
    
    ;storage room (the spawn point of scp-049)
    ;r\Objects[5] = CreatePivot(r\obj)
    ;PositionEntity(r\Objects[5], r\x + 584.0 * RoomScale, -3440.0 * RoomScale, r\z + 104.0 * RoomScale, True)
    
    ;zombie 1 and 049
    r\Objects[4] = CreatePivot(r\obj)
    PositionEntity(r\Objects[4], r\x + 528.0 * RoomScale, -3440.0 * RoomScale, r\z + 96.0 * RoomScale, True)
    ;zombie 2
    r\Objects[5] = CreatePivot(r\obj)
    PositionEntity(r\Objects[5], r\x  + 64.0 * RoomScale, -3440.0 * RoomScale, r\z - 1000.0 * RoomScale, True)
    
    For n% = 0 To 1
        r\Objects[n * 2 + 6] = CopyEntity(LeverBaseOBJ)
        r\Objects[n * 2 + 7] = CopyEntity(LeverOBJ)
        
        r\Levers[n] = r\Objects[n * 2 + 7]
        
        For i% = 0 To 1
            ScaleEntity(r\Objects[n * 2 + 6 + i], 0.03, 0.03, 0.03)
            
            Select n
                Case 0 ;power feed
                    PositionEntity (r\Objects[n * 2 + 6 + i], r\x - 328.0 * RoomScale, r\y - 3374.0 * RoomScale, r\z + 916 * RoomScale, True)
                    
                Case 1 ;generator
                    PositionEntity (r\Objects[n * 2 + 6 + i], r\x - 370.0 * RoomScale, r\y - 3400.0 * RoomScale, r\z - 799 * RoomScale, True)
                    
            End Select
            
            EntityParent(r\Objects[n * 2 + 6 + i], r\obj)
        Next
        
        RotateEntity(r\Objects[n*2+6], 0, -180*n, 0)
        RotateEntity(r\Objects[n*2+7], 81-92*n, -180*(Not n), 0)
        
        EntityPickMode r\Objects[n * 2 + 7], 1, False
        EntityRadius r\Objects[n * 2 + 7], 0.1
    Next
    
    
    r\RoomDoors[0] = CreateDoor(r\zone, r\x + 328.0 * RoomScale, 0.0, r\z + 656.0 * RoomScale, 90, r, True)
    r\RoomDoors[0]\AutoClose = False : r\RoomDoors[0]\open = True
    PositionEntity(r\RoomDoors[0]\buttons[1], r\x + 288.0 * RoomScale, 0.7, r\z + 512.0 * RoomScale, True)
    PositionEntity(r\RoomDoors[0]\buttons[0], r\x + 368.0 * RoomScale, 0.7, r\z + 840.0 * RoomScale, True)			
    
    r\RoomDoors[1] = CreateDoor(r\zone, r\x - 2328.0 * RoomScale, -3520.0 * RoomScale, r\z - 656.0 * RoomScale, 90, r, False)
    r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\open = False	
    PositionEntity(r\RoomDoors[1]\buttons[1], r\x - 2432.0 * RoomScale, EntityY(r\RoomDoors[1]\buttons[1],True), r\z - 816.0 * RoomScale, True)
    PositionEntity(r\RoomDoors[1]\buttons[0], r\x - 2304.0 * RoomScale, EntityY(r\RoomDoors[1]\buttons[0],True), r\z - 472.0 * RoomScale, True)				
    
    r\RoomDoors[2] = CreateDoor(r\zone, r\x - 328.0 * RoomScale, 0.0, r\z - 656.0 * RoomScale, 90, r, True)
    r\RoomDoors[2]\AutoClose = False : r\RoomDoors[2]\open = True
    PositionEntity(r\RoomDoors[2]\buttons[0], r\x - 288.0 * RoomScale, 0.7, r\z - 512.0 * RoomScale, True)
    PositionEntity(r\RoomDoors[2]\buttons[1], r\x - 368.0 * RoomScale, 0.7, r\z - 840.0 * RoomScale, True)				
    
    r\RoomDoors[3] = CreateDoor(r\zone, r\x + 2360.0 * RoomScale, -3520.0 * RoomScale, r\z + 656.0 * RoomScale, 90, r, False)
    r\RoomDoors[3]\AutoClose = False : r\RoomDoors[3]\open = False		
    PositionEntity(r\RoomDoors[3]\buttons[0], r\x + 2432.0 * RoomScale, EntityY(r\RoomDoors[3]\buttons[0],True), r\z + 816.0 * RoomScale, True)
    PositionEntity(r\RoomDoors[3]\buttons[1], r\x + 2312.0 * RoomScale, EntityY(r\RoomDoors[3]\buttons[1],True), r\z + 472.0 * RoomScale, True)	
    
    For i = 0 To 3
        If (i Mod 2) = 1
            AssignElevatorObj(r\Objects[i],r\RoomDoors[i],2)
        Else
            AssignElevatorObj(r\Objects[i],r\RoomDoors[i],True)
        EndIf
    Next
    
    ;storage room door
    r\RoomDoors[4] = CreateDoor(r\zone, r\x + 272.0 * RoomScale, -3552.0 * RoomScale, r\z + 104.0 * RoomScale, 90, r, False)
    r\RoomDoors[4]\AutoClose = False : r\RoomDoors[4]\open = False : r\RoomDoors[4]\locked = True
    
    d.Doors = CreateDoor(0, r\x,0,r\z, 0, r, False, 2, -2)
    
    it = CreateItem("Document SCP-049", "paper", r\x - 608.0 * RoomScale, r\y - 3332.0 * RoomScale, r\z + 876.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Level 4 Key Card", "key4", r\x - 512.0 * RoomScale, r\y - 3412.0 * RoomScale, r\z + 864.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("First Aid Kit", "firstaid", r\x +385.0 * RoomScale, r\y - 3412.0 * RoomScale, r\z + 271.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    r\Objects[10] = LoadMesh_Strict("GFX\map\room049_hb.b3d",r\obj)
    EntityPickMode r\Objects[10],2
    EntityType r\Objects[10],HIT_MAP
    EntityAlpha r\Objects[10],0.0
End Function
