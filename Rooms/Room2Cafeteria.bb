Function FillRoom2Cafeteria(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    ;scp-294
    r\Objects[0] = CreatePivot(r\obj)
    PositionEntity(r\Objects[0], r\x+1847.0*RoomScale, -240.0*RoomScale, r\z-321*RoomScale, True)
    ;"spawnpoint" for the cups
    r\Objects[1] = CreatePivot(r\obj)
    PositionEntity(r\Objects[1], r\x+1780.0*RoomScale, -248.0*RoomScale, r\z-276*RoomScale, True)
    
    it = CreateItem("cup", "cup", r\x-508.0*RoomScale, -187*RoomScale, r\z+284.0*RoomScale, 240,175,70)
    EntityParent(it\collider, r\obj) : it\name = "Cup of Orange Juice"
    
    it = CreateItem("cup", "cup", r\x+1412 * RoomScale, -187*RoomScale, r\z-716.0 * RoomScale, 87,62,45)
    EntityParent(it\collider, r\obj) : it\name = "Cup of Coffee"
    
    it = CreateItem("Empty Cup", "emptycup", r\x-540*RoomScale, -187*RoomScale, r\z+124.0*RoomScale)
    EntityParent(it\collider, r\obj)
End Function
