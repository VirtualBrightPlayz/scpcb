Function FillRoom2Tesla(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump
    
    r\Objects[0] = CreatePivot()
    PositionEntity(r\Objects[0], r\x - 114.0 * RoomScale, 0.0, r\z)
    EntityParent(r\Objects[0], r\obj)
    
    r\Objects[1] = CreatePivot()
    PositionEntity(r\Objects[1], r\x + 114.0 * RoomScale, 0.0, r\z)
    EntityParent(r\Objects[1], r\obj)			
    
    r\Objects[2] = CreatePivot()
    PositionEntity(r\Objects[2], r\x, 0.0, r\z)
    EntityParent(r\Objects[2], r\obj)	
    
    r\Objects[3] = CreateSprite()
    EntityTexture (r\Objects[3], TeslaTexture)
    SpriteViewMode(r\Objects[3],2) 
    ;ScaleSprite (r\Objects[3],((512.0 * RoomScale)/2.0),((512.0 * RoomScale)/2.0))
    EntityBlend (r\Objects[3], 3) 
    EntityFX(r\Objects[3], 1 + 8 + 16)
    
    PositionEntity(r\Objects[3], r\x, 0.8, r\z)
    
    HideEntity r\Objects[3]
    EntityParent(r\Objects[3], r\obj)
    
    w.waypoints = CreateWaypoint(r\x, r\y + 66.0 * RoomScale, r\z + 292.0 * RoomScale, Null, r)
    w2.waypoints = CreateWaypoint(r\x, r\y + 66.0 * RoomScale, r\z - 284.0 * RoomScale, Null, r)
    w\connected[0] = w2 : w\dist[0] = EntityDistance(w\obj, w2\obj)
    w2\connected[0] = w : w2\dist[0] = w\dist[0]
    
    r\Objects[4] = CreateSprite()
    PositionEntity(r\Objects[4], r\x - 32 * RoomScale, 568 * RoomScale, r\z)
    ScaleSprite(r\Objects[4], 0.03, 0.03)
    EntityTexture(r\Objects[4], LightSpriteTex(1))
    EntityBlend (r\Objects[4], 3)
    EntityParent(r\Objects[4], r\obj)
    HideEntity r\Objects[4]
    
    r\Objects[5] = CreatePivot()
    PositionEntity(r\Objects[5],r\x,0,r\z-800*RoomScale)
    EntityParent(r\Objects[5],r\obj)
    
    r\Objects[6] = CreatePivot()
    PositionEntity(r\Objects[6],r\x,0,r\z+800*RoomScale)
    EntityParent(r\Objects[6],r\obj)
End Function
