Function FillRoom_off_plain_2(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump
    
    it = CreateItem("Document SCP-106", "paper", r\x + 404.0 * RoomScale, r\y + 145.0 * RoomScale, r\z + 559.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Level 2 Key Card", "key2", r\x - 156.0 * RoomScale, r\y + 151.0 * RoomScale, r\z + 72.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("S-NAV 300 Navigator", "nav", r\x + 305.0 * RoomScale, r\y + 153.0 * RoomScale, r\z + 944.0 * RoomScale)
    it\state = 20 : EntityParent(it\collider, r\obj)
    
    it = CreateItem("Notification", "paper", r\x -137.0 * RoomScale, r\y + 153.0 * RoomScale, r\z + 464.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    w.waypoints = CreateWaypoint(r\x - 32.0 * RoomScale, r\y + 66.0 * RoomScale, r\z + 288.0 * RoomScale, Null, r)
    w2.waypoints = CreateWaypoint(r\x, r\y + 66.0 * RoomScale, r\z - 448.0 * RoomScale, Null, r)
    w\connected[0] = w2 : w\dist[0] = EntityDistance(w\obj, w2\obj)
    w2\connected[0] = w : w2\dist[0] = w\dist[0]
End Function
