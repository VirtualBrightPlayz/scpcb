Function FillRoom372(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
	Local it.Items, i%
	Local xtemp%, ytemp%, ztemp%
	
	Local t1;, Bump
    
    it = CreateItem("Document SCP-372", "paper", r\x + 800.0 * RoomScale, r\y + 176.0 * RoomScale, r\z + 1108.0 * RoomScale)
    RotateEntity it\collider, 0, r\angle, 0
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Radio Transceiver", "radio", r\x + 800.0 * RoomScale, r\y + 112.0 * RoomScale, r\z + 944.0 * RoomScale)
    it\state = 80.0
    EntityParent(it\collider, r\obj)
    
    r\Objects[3] = LoadMesh_Strict("GFX\map\372_hb.b3d",r\obj)
    EntityPickMode r\Objects[3],2
    EntityType r\Objects[3],HIT_MAP
    EntityAlpha r\Objects[3],0.0
End Function
