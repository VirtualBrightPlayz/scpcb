Function FillRoom_off_bain_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump
    
    d.Doors = CreateDoor(0, r\x - 240.0 * RoomScale, 0.0, r\z, 90, r, False)
    d\open = False : d\AutoClose = False
End Function
