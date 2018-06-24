Function FillRoom_hll_dirty_3(r.Room)
    Local d.Door, d2.Door, sc.SecurityCam, de.Decal, r2.Room, sc2.SecurityCam
    Local it.Item, i%
    Local xtemp%, ytemp%, ztemp%

    Local t1%;, Bump

    For r2 = Each Room
        If (r2\roomTemplate\name = r\roomTemplate\name And r2 <> r) Then
            r\objects[0] = CopyEntity(r2\objects[0],r\obj)
            Exit
        EndIf
    Next
    ;If (r\objects[0]=0) Then r\objects[0] = LoadMesh("GFX/Map/room3z2_hb.b3d",r\obj)
    ;EntityPickMode(r\objects[0],2)
    ;EntityType(r\objects[0],HIT_MAP)
    ;EntityAlpha(r\objects[0],0.0)
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D