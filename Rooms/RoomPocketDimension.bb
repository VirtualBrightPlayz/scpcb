Function FillRoomPocketDimension(r.Rooms)
    Local d.Doors, d2.Doors, sc.SecurityCams, de.Decals, r2.Rooms, sc2.SecurityCams
    Local it.Items, i%
    Local xtemp%, ytemp%, ztemp%
    
    Local t1;, Bump

    Local hallway = LoadMesh_Strict("GFX\map\pocketdimension2.b3d") ;the tunnels in the first room
    r\Objects[8]=LoadMesh_Strict("GFX\map\pocketdimension3.b3d")	;the room with the throne, moving pillars etc 
    r\Objects[9]=LoadMesh_Strict("GFX\map\pocketdimension4.b3d") ;the flying pillar
    r\Objects[10]=CopyEntity(r\Objects[9])
    
    r\Objects[11]=LoadMesh_Strict("GFX\map\pocketdimension5.b3d") ;the pillar room
    
    
    terrain = LoadMesh_Strict("GFX\map\pocketdimensionterrain.b3d")
    ScaleEntity terrain,RoomScale,RoomScale,RoomScale,True
    ;RotateEntity terrain,0,e\room\angle,0,True
    PositionEntity terrain, 0, 2944, 0, True
    
    
    
    CreateItem("Burnt Note", "paper", EntityX(r\obj),0.5,EntityZ(r\obj)+3.5)
    
    For n = 0 To -1;4
        
        Select n
            Case 0
                entity = hallway 					
            Case 1
                entity = r\Objects[8]						
            Case 2
                entity = r\Objects[9]						
            Case 3
                entity = r\Objects[10]							
            Case 4
                entity = r\Objects[11]							
        End Select 
        
        ;If BumpEnabled Then 
        ;	
        ;	For i = 1 To CountSurfaces(entity)
        ;		sf = GetSurface(entity,i)
        ;		b = GetSurfaceBrush( sf )
        ;		t = GetBrushTexture(b,1)
        ;		texname$ =  StripPath(TextureName(t))
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
        
    Next
    
    For i = 8 To 11
        ScaleEntity (r\Objects[i],RoomScale,RoomScale,RoomScale)
        EntityType r\Objects[i], HIT_MAP
        EntityPickMode r\Objects[i], 3
        PositionEntity(r\Objects[i],r\x,r\y,r\z+32.0,True)
    Next
    
    ScaleEntity (terrain,RoomScale,RoomScale,RoomScale)
    EntityType terrain, HIT_MAP
    EntityPickMode terrain, 3
    PositionEntity(terrain,r\x,r\y+2944.0*RoomScale,r\z+32.0,True)			
    
    r\RoomDoors[0] = CreateDoor(0, r\x,2048*RoomScale,r\z+32.0-1024*RoomScale,0,r,False)
    r\RoomDoors[1] = CreateDoor(0, r\x,2048*RoomScale,r\z+32.0+1024*RoomScale,180,r,False)
    
    de.Decals = CreateDecal(18, r\x-(1536*RoomScale), 0.02,r\z+608*RoomScale+32.0, 90,0,0)
    EntityParent(de\obj, r\obj)
    de\Size = Rnd(0.8, 0.8)
    de\blendmode = 2
    de\fx = 1+8
    ScaleSprite(de\obj, de\Size, de\Size)
    EntityFX(de\obj, 1+8)
    EntityBlend de\obj, 2
    
    ScaleEntity (r\Objects[10],RoomScale*1.5,RoomScale*2.0,RoomScale*1.5,True)			
    PositionEntity(r\Objects[11],r\x,r\y,r\z+64.0,True)			
    
    For i = 1 To 8
        r\Objects[i-1] = CopyEntity(hallway) ;CopyMesh
        ScaleEntity (r\Objects[i-1],RoomScale,RoomScale,RoomScale)
        angle# = (i-1) * (360.0/8.0)
        
        EntityType r\Objects[i-1], HIT_MAP
        EntityPickMode r\Objects[i-1], 3		
        
        RotateEntity(r\Objects[i-1],0,angle-90,0)
        PositionEntity(r\Objects[i-1],r\x+Cos(angle)*(512.0*RoomScale),0.0,r\z+Sin(angle)*(512.0*RoomScale))
        EntityParent (r\Objects[i-1], r\obj)
        
        If i < 6 Then 
            de.Decals = CreateDecal(i+7, r\x+Cos(angle)*(512.0*RoomScale)*3.0, 0.02,r\z+Sin(angle)*(512.0*RoomScale)*3.0, 90,angle-90,0)
            de\Size = Rnd(0.5, 0.5)
            de\blendmode = 2
            de\fx = 1+8
            ScaleSprite(de\obj, de\Size, de\Size)
            EntityFX(de\obj, 1+8)
            EntityBlend de\obj, 2
        EndIf				
    Next
    
    For i = 12 To 16
        r\Objects[i] = CreatePivot(r\Objects[11])
        Select i
            Case 12
                PositionEntity(r\Objects[i],r\x,r\y+200*RoomScale,r\z+64.0,True)	
            Case 13
                PositionEntity(r\Objects[i],r\x+390*RoomScale,r\y+200*RoomScale,r\z+64.0+272*RoomScale,True)	
            Case 14
                PositionEntity(r\Objects[i],r\x+838*RoomScale,r\y+200*RoomScale,r\z+64.0-551*RoomScale,True)	
            Case 15
                PositionEntity(r\Objects[i],r\x-139*RoomScale,r\y+200*RoomScale,r\z+64.0+1201*RoomScale,True)	
            Case 16
                PositionEntity(r\Objects[i],r\x-1238*RoomScale,r\y-1664*RoomScale,r\z+64.0+381*RoomScale,True)
        End Select 
        
    Next
    
    Local OldManEyes% = LoadTexture_Strict("GFX\npcs\oldmaneyes.jpg")
    r\Objects[17] = CreateSprite()
    ScaleSprite(r\Objects[17], 0.03, 0.03)
    EntityTexture(r\Objects[17], OldManEyes)
    EntityBlend (r\Objects[17], 3)
    EntityFX(r\Objects[17], 1 + 8)
    SpriteViewMode(r\Objects[17], 2)
    
    r\Objects[18] = LoadTexture_Strict("GFX\npcs\pdplane.png", 1+2)
    r\Objects[19] = LoadTexture_Strict("GFX\npcs\pdplaneeye.png", 1+2)		
    
    r\Objects[20] = CreateSprite()
    ScaleSprite(r\Objects[20], 8.0, 8.0)
    EntityTexture(r\Objects[20], r\Objects[18])
    EntityOrder r\Objects[20], 100
    EntityBlend (r\Objects[20], 2)
    EntityFX(r\Objects[20], 1 + 8)
    SpriteViewMode(r\Objects[20], 2)
    
    FreeTexture t
    FreeEntity hallway
End Function
