Function FillRoom178(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump

    r\RoomDoors[0] = CreateDoor(r\zone,r\x,r\y,r\z - 672.0*RoomScale,0,r,False,False,3)
    
    it = CreateItem("SCP-178","scp178",r\x,200.0 * RoomScale,r\z - 80.0 * RoomScale)
    RotateEntity it\collider,0,0,0
    EntityParent it\collider,r\obj
    SetAnimTime it\model,0.0
End Function
