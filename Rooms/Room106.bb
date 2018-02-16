Function FillRoom106(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump

    it = CreateItem("Level 5 Key Card", "key5", r\x - 752.0 * RoomScale, r\y - 592 * RoomScale, r\z + 3026.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Dr. Allok's Note", "paper", r\x - 416.0 * RoomScale, r\y - 576 * RoomScale, r\z + 2492.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    it = CreateItem("Recall Protocol RP-106-N", "paper", r\x + 268.0 * RoomScale, r\y - 576 * RoomScale, r\z + 2593.0 * RoomScale)
    EntityParent(it\collider, r\obj)
    
    d = CreateDoor(r\zone, r\x - 968.0 * RoomScale, -764.0 * RoomScale, r\z + 1392.0 * RoomScale, 0, r, False, False, 4)
    d\AutoClose = False : d\open = False	
    
    d = CreateDoor(r\zone, r\x, 0, r\z - 464.0 * RoomScale, 0, r, False, False, 3)
    d\AutoClose = False : d\open = False			
    
    d = CreateDoor(r\zone, r\x - 624.0 * RoomScale, -1280.0 * RoomScale, r\z, 90, r, False, False, 4)
    d\AutoClose = False : d\open = False	
    
    r\Objects[6] = LoadMesh_Strict("GFX\map\room1062.b3d")
    
    ScaleEntity (r\Objects[6],RoomScale,RoomScale,RoomScale)
    EntityType r\Objects[6], HIT_MAP
    EntityPickMode r\Objects[6], 3
    PositionEntity(r\Objects[6],r\x+784.0*RoomScale,-980.0*RoomScale,r\z+720.0*RoomScale,True)
    
    ;If BumpEnabled Then 
    ;	
    ;	For i = 1 To CountSurfaces(r\Objects[6])
    ;		sf = GetSurface(r\Objects[6],i)
    ;		b = GetSurfaceBrush( sf )
    ;		t = GetBrushTexture(b,1)
    ;		texname$ =  StripPath(TextureName(t))
    ;		
    ;		mat.Materials=GetCache(texname)
    ;		If mat<>Null Then
    ;			If mat\Bump<>0 Then
    ;				t1 = GetBrushTexture(b,0)
    ;				
    ;				BrushTexture b, t1, 0, 0	
    ;				BrushTexture b, mat\Bump, 0, 1
    ;				BrushTexture b, t, 0, 2					
    ;				
    ;				PaintSurface sf,b
    ;				
    ;				If t1<>0 Then FreeTexture t1 : t1=0
    ;			EndIf
    ;		EndIf
    ;		
    ;		If t<>0 Then FreeTexture t : t=0
    ;		If b<>0 Then FreeBrush b : b=0
    ;	Next
    ;	
    ;EndIf
    
    EntityParent(r\Objects[6], r\obj)
    
    For n = 0 To 2 Step 2
        r\Objects[n] = CopyEntity(LeverBaseOBJ)
        r\Objects[n+1] = CopyEntity(LeverOBJ)
        
        r\Levers[n/2] = r\Objects[n+1]
        
        For i% = 0 To 1
            ScaleEntity(r\Objects[n+i], 0.04, 0.04, 0.04)
            PositionEntity (r\Objects[n+i], r\x - (555.0 - 81.0 * (n/2)) * RoomScale, r\y - 576.0 * RoomScale, r\z + 3040.0 * RoomScale, True)
            
            EntityParent(r\Objects[n+i], r\obj)
        Next
        RotateEntity(r\Objects[n], 0, 0, 0)
        RotateEntity(r\Objects[n+1], 10, -180, 0)
        
        ;EntityPickMode(r\Objects[n * 2 + 1], 2)
        EntityPickMode r\Objects[n+1], 1, False
        EntityRadius r\Objects[n+1], 0.1
        ;makecollbox(r\Objects[n * 2 + 1])
    Next
    
    RotateEntity(r\Objects[1], 81,-180,0)
    RotateEntity(r\Objects[3], -81,-180,0)			
    
    r\Objects[4] = CreateButton(r\x - 146.0*RoomScale, r\y - 576.0 * RoomScale, r\z + 3045.0 * RoomScale, 0,0,0)
    EntityParent (r\Objects[4],r\obj)
    
    sc.SecurityCams = CreateSecurityCam(r\x + 768.0 * RoomScale, r\y + 1392.0 * RoomScale, r\z + 1696.0 * RoomScale, r, True)
    sc\angle = 45 + 90 + 180
    sc\turn = 20
    TurnEntity(sc\CameraObj, 45, 0, 0)
    EntityParent(sc\obj, r\obj)
    
    r\Objects[7] = sc\CameraObj
    r\Objects[8] = sc\obj
    
    PositionEntity(sc\ScrObj, r\x - 272.0 * RoomScale, -544.0 * RoomScale, r\z + 3020.0 * RoomScale)
    TurnEntity(sc\ScrObj, 0, -10, 0)
    EntityParent sc\ScrObj, r\obj
    sc\CoffinEffect=0
    
    ;r\NPC[0] = CreateNPC(NPCtypeD, r\x + 1088.0 * RoomScale, 1096.0 * RoomScale, r\z + 1728.0 * RoomScale)
    r\Objects[5] = CreatePivot()
    TurnEntity r\Objects[5], 0,180,0
    PositionEntity (r\Objects[5], r\x + 1088.0 * RoomScale, 1104.0 * RoomScale, r\z + 1888.0 * RoomScale) 
    EntityParent r\Objects[5], r\obj
    ;HideEntity r\NPC[0]\obj
    
    r\Objects[9] = CreatePivot(r\obj)
    PositionEntity (r\Objects[9], r\x - 272 * RoomScale, r\y - 672.0 * RoomScale, r\z + 2736.0 * RoomScale, True)
    
    sc.SecurityCams = CreateSecurityCam(r\x-1216.0*RoomScale, r\y-336.0*RoomScale, r\z+1468.0*RoomScale, r, True)
    sc\angle = 315
    sc\turn = 45
    sc\room = r
    TurnEntity(sc\CameraObj, 20, 0, 0)
    EntityParent(sc\obj, r\obj)
    sc\ID = 4
End Function
