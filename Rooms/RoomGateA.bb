Function FillRoomGateA(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump

    r\RoomDoors[2] = CreateDoor(r\zone, r\x - 4064.0 * RoomScale, (-1280.0+12000.0)*RoomScale, r\z + 3952.0 * RoomScale, 0, r, False)
    r\RoomDoors[2]\AutoClose = False : r\RoomDoors[2]\open = False
    
    d2 = CreateDoor(r\zone, r\x, 12000.0*RoomScale, r\z - 1024.0 * RoomScale, 0, r, False)
    d2\AutoClose = False : d2\open = False : d2\locked = True
    
    d2 = CreateDoor(r\zone, r\x-1440*RoomScale, (12000.0-480.0)*RoomScale, r\z + 2328.0 * RoomScale, 0, r, False, False, 2)
    If SelectedEnding = "A2" Then 
        d2\AutoClose = False : d2\open = True : d2\locked = True	
    Else
        d2\AutoClose = False : d2\open = False : d2\locked = False	
    EndIf	
    PositionEntity d2\buttons[0], r\x-1320.0*RoomScale, EntityY(d2\buttons[0],True), r\z + 2288.0*RoomScale, True
    PositionEntity d2\buttons[1], r\x-1584*RoomScale, EntityY(d2\buttons[0],True), r\z + 2488.0*RoomScale, True	
    RotateEntity d2\buttons[1], 0, 90, 0, True
    
    d2 = CreateDoor(r\zone, r\x-1440*RoomScale, (12000.0-480.0)*RoomScale, r\z + 4352.0 * RoomScale, 0, r, False, False, 2)
    If SelectedEnding = "A2" Then 
        d2\AutoClose = False : d2\open = True : d2\locked = True	
    Else
        d2\AutoClose = False : d2\open = False : d2\locked = False
    EndIf
    PositionEntity d2\buttons[0], r\x-1320.0*RoomScale, EntityY(d2\buttons[0],True), r\z + 4384.0*RoomScale, True
    RotateEntity d2\buttons[0], 0, 180, 0, True	
    PositionEntity d2\buttons[1], r\x-1584.0*RoomScale, EntityY(d2\buttons[0],True), r\z + 4232.0*RoomScale, True	
    RotateEntity d2\buttons[1], 0, 90, 0, True	
    
    For r2.Rooms = Each Rooms
        If r2\RoomTemplate\Name = "exit1" Then
            r\Objects[1]=r2\Objects[1]
            r\Objects[2]=r2\Objects[2]	
        ElseIf r2\RoomTemplate\Name = "gateaentrance"
            ;ylempi hissi
            r\RoomDoors[1] = CreateDoor(0, r\x+1544.0*RoomScale,12000.0*RoomScale, r\z-64.0*RoomScale, 90, r, False)
            r\RoomDoors[1]\AutoClose = False : r\RoomDoors[1]\open = False
            PositionEntity(r\RoomDoors[1]\buttons[0],r\x+1584*RoomScale, EntityY(r\RoomDoors[1]\buttons[0],True), r\z+80*RoomScale, True)
            PositionEntity(r\RoomDoors[1]\buttons[1],r\x+1456*RoomScale, EntityY(r\RoomDoors[1]\buttons[1],True), r\z-208*RoomScale, True)	
            r2\Objects[1] = CreatePivot()
            PositionEntity(r2\Objects[1], r\x+1848.0*RoomScale, 240.0*RoomScale, r\z-64.0*RoomScale, True)
            EntityParent r2\Objects[1], r\obj						
        EndIf
    Next
    
    ;106:n spawnpoint
    r\Objects[3]=CreatePivot()
    PositionEntity(r\Objects[3], r\x+1216.0*RoomScale, 0, r\z+2112.0*RoomScale, True)
    EntityParent r\Objects[3], r\obj
    
    ;sillan loppup��
    r\Objects[4]=CreatePivot()
    PositionEntity(r\Objects[4], r\x, 96.0*RoomScale, r\z+6400.0*RoomScale, True)
    EntityParent r\Objects[4], r\obj		
    
    ;vartiotorni 1
    r\Objects[5]=CreatePivot()
    PositionEntity(r\Objects[5], r\x+1784.0*RoomScale, 2124.0*RoomScale, r\z+4512.0*RoomScale, True)
    EntityParent r\Objects[5], r\obj	
    
    ;vartiotorni 2
    r\Objects[6]=CreatePivot()
    PositionEntity(r\Objects[6], r\x-5048.0*RoomScale, 1912.0*RoomScale, r\z+4656.0*RoomScale, True)
    EntityParent r\Objects[6], r\obj	
    
    ;sillan takareuna
    r\Objects[7]=CreatePivot()
    PositionEntity(r\Objects[7], r\x+1824.0*RoomScale, 224.0*RoomScale, r\z+7056.0*RoomScale, True)
    EntityParent r\Objects[7], r\obj	
    
    ;sillan takareuna2
    r\Objects[8]=CreatePivot()
    PositionEntity(r\Objects[8], r\x-1824.0*RoomScale, 224.0*RoomScale, r\z+7056.0*RoomScale, True)
    EntityParent r\Objects[8], r\obj	
    
    ;"valopyssy"
    r\Objects[9]=CreatePivot()
    PositionEntity(r\Objects[9], r\x+2624.0*RoomScale, 992.0*RoomScale, r\z+6157.0*RoomScale, True)
    EntityParent r\Objects[9], r\obj	
    ;objects[10] = valopyssyn yl�osa
    
    ;tunnelin loppu
    r\Objects[11]=CreatePivot()
    PositionEntity(r\Objects[11], r\x-4064.0*RoomScale, -1248.0*RoomScale, r\z-1696.0*RoomScale, True)
    EntityParent r\Objects[11], r\obj
    
    r\Objects[13]=LoadMesh_Strict("GFX\map\gateawall1.b3d",r\obj)
    PositionEntity(r\Objects[13], r\x-4308.0*RoomScale, -1045.0*RoomScale, r\z+544.0*RoomScale, True)
    EntityColor r\Objects[13], 25,25,25
    ;EntityFX(r\Objects[13],1)
    
    r\Objects[14]=LoadMesh_Strict("GFX\map\gateawall2.b3d",r\obj)
    PositionEntity(r\Objects[14], r\x-3820.0*RoomScale, -1045.0*RoomScale, r\z+544.0*RoomScale, True)	
    EntityColor r\Objects[14], 25,25,25
    ;EntityFX(r\Objects[14],1)
    
    r\Objects[15]=CreatePivot(r\obj)
    PositionEntity(r\Objects[15], r\x-3568.0*RoomScale, -1089.0*RoomScale, r\z+4944.0*RoomScale, True)
End Function