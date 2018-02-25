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


Function UpdateEventPj(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If mainPlayer\currRoom = e\room Then
		If e\EventState = 0 Then
			If EntityDistance(mainPlayer\collider, e\room\obj) < 2.5 Then
				PlaySound_Strict(RustleSFX(Rand(0,2)))
				CreateNPC(NPCtype372, 0, 0, 0)
				e\EventState = 1
				RemoveEvent(e)
			EndIf					
		EndIf
	EndIf
	;[End Block]
End Function

