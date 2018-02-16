Function FillRoom2_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    For r2.Rooms = Each Rooms
        If r2<>r Then
            If r2\RoomTemplate\Name = "room2_2" Then
                r\Objects[0] = CopyEntity(r2\Objects[0]) ;don't load the mesh again
                Exit
            EndIf
        EndIf
    Next
    If r\Objects[0]=0 Then r\Objects[0] = LoadMesh("GFX\map\fan.b3d")
    ScaleEntity r\Objects[0], RoomScale, RoomScale, RoomScale
    PositionEntity(r\Objects[0], r\x - 248 * RoomScale, 528 * RoomScale, r\z, 0)
    EntityParent(r\Objects[0], r\obj)
End Function
