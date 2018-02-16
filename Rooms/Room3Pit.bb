Function FillRoom3Pit(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    em.Emitters = CreateEmitter(r\x + 512.0 * RoomScale, -76 * RoomScale, r\z - 688 * RoomScale, 0)
    TurnEntity(em\Obj, -90, 0, 0)
    EntityParent(em\Obj, r\obj)
    em\RandAngle = 55
    em\Speed = 0.0005
    em\Achange = -0.015
    em\SizeChange = 0.007
    
    em.Emitters = CreateEmitter(r\x - 512.0 * RoomScale, -76 * RoomScale, r\z - 688 * RoomScale, 0)
    TurnEntity(em\Obj, -90, 0, 0)
    EntityParent(em\Obj, r\obj)
    em\RandAngle = 55
    em\Speed = 0.0005
    em\Achange = -0.015
    em\SizeChange = 0.007
    
    r\Objects[0]= CreatePivot(r\obj)
    PositionEntity(r\Objects[0], r\x + 704.0 * RoomScale, 112.0*RoomScale, r\z-416.0*RoomScale, True)
End Function
