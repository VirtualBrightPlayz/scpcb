Function FillRoom_hll_dirty_3(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump
    
    For r2.Rooms = Each Rooms
        If r2\RoomTemplate\Name = r\RoomTemplate\Name And r2 <> r
            r\Objects[0] = CopyEntity(r2\Objects[0],r\obj)
            Exit
        EndIf
    Next
    ;If r\Objects[0]=0 Then r\Objects[0] = LoadMesh("GFX/map/room3z2_hb.b3d",r\obj)
    ;EntityPickMode r\Objects[0],2
    ;EntityType r\Objects[0],HIT_MAP
    ;EntityAlpha r\Objects[0],0.0
End Function
