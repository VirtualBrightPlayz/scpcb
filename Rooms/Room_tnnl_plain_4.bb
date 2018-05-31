

Function UpdateEvent_tnnl_plain_4(e.Events)
	Local dist#, i%, temp%, pvt%, strtemp$, j%, k%

	Local p.Particles, n.NPCs, r.Rooms, e2.Events, it.Items, em.Emitters, sc.SecurityCams, sc2.SecurityCams

	Local CurrTrigger$ = ""

	Local x#, y#, z#

	Local angle#

	;[Block]
	If e\room\dist < 10.0 And e\room\dist > 0 Then
		e\room\NPC[0]=CreateNPC(NPCtypeD, EntityX(e\room\obj,True)+1.0, 0.5, EntityZ(e\room\obj,True)+1.0)
		e\room\NPC[0]\texture = "GFX/npcs/body1.jpg"
		tex = LoadTexture(e\room\NPC[0]\texture)
		EntityTexture(e\room\NPC[0]\obj, tex)
		FreeTexture tex
		;EntityTexture e\room\NPC[0]\obj,NTF_BodyTextures(0)
		
		RotateEntity e\room\NPC[0]\Collider, 0, EntityYaw(e\room\obj)-(Rand(20,60)),0, True	
		
		SetNPCFrame e\room\NPC[0], 19
		e\room\NPC[0]\State=8
		
		;Delete e
		RemoveEvent(e)
	EndIf
	;[End Block]
End Function

