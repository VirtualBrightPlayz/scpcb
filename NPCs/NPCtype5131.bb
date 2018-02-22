Function InitializeNPCtype5131(n.NPCs)
    n\NVName = "SCP-513-1"
    n\Collider = CreatePivot()
    EntityRadius n\Collider, 0.2
    n\obj = LoadAnimMesh_Strict("GFX\npcs\bll.b3d")
    
    n\obj2 = CopyEntity (n\obj)
    EntityAlpha n\obj2, 0.6
    
    temp# = 1.8 / MeshWidth(n\obj)
    ScaleEntity n\obj, temp, temp, temp
    ScaleEntity n\obj2, temp, temp, temp
End Function

Function UpdateNPCtype5131(n.NPCs)
    If mainPlayer\currRoom\RoomTemplate\Name <> "pocketdimension" Then 
        If n\Idle Then
            HideEntity(n\obj)
            HideEntity(n\obj2)
            If Rand(200)=1 Then
                For w.WayPoints = Each WayPoints
                    If w\room<>mainPlayer\currRoom Then
                        x = Abs(EntityX(mainPlayer\collider)-EntityX(w\obj,True))
                        If x>3 And x < 9 Then
                            z = Abs(EntityZ(mainPlayer\collider)-EntityZ(w\obj,True))
                            If z>3 And z < 9 Then
                                PositionEntity(n\Collider, EntityX(w\obj,True), EntityY(w\obj,True), EntityZ(w\obj,True))
                                PositionEntity(n\obj, EntityX(w\obj,True), EntityY(w\obj,True), EntityZ(w\obj,True))
                                ResetEntity n\Collider
                                ShowEntity(n\obj)
                                ShowEntity(n\obj2)
                                
                                n\LastSeen = 0
                                
                                n\Path[0]=w
                                
                                n\Idle = False
                                n\State2 = Rand(15,20)*70
                                n\State = Max(Rand(-1,2),0)
                                Exit
                            EndIf
                        EndIf
                    EndIf
                Next
            End If
        Else
            dist = EntityDistance(mainPlayer\collider, n\Collider)
            
            ;use the prev-values to do a "twitching" effect"
            n\PrevX = CurveValue(0.0, n\PrevX, 10.0)
            n\PrevZ = CurveValue(0.0, n\PrevZ, 10.0)
            
            If Rand(100)=1 Then
                If Rand(5)=1 Then
                    n\PrevX = (EntityX(mainPlayer\collider)-EntityX(n\Collider))*0.9
                    n\PrevZ = (EntityZ(mainPlayer\collider)-EntityZ(n\Collider))*0.9
                Else
                    n\PrevX = Rnd(0.1,0.5)
                    n\PrevZ = Rnd(0.1,0.5)						
                EndIf
            EndIf
            
            temp = Rnd(-1.0,1.0)
            PositionEntity n\obj2, EntityX(n\Collider)+n\PrevX*temp, EntityY(n\Collider) - 0.2 + Sin((MilliSecs2()/8-45) Mod 360)*0.05, EntityZ(n\Collider)+n\PrevZ*temp
            RotateEntity n\obj2, 0, EntityYaw(n\obj), 0
            If (Floor(AnimTime(n\obj2))<>Floor(n\Frame)) Then SetAnimTime n\obj2, n\Frame
            
            If n\State = 0 Then
                AnimateNPC(n, 229, 299, 0.2)
                ;Animate2(n\obj, AnimTime(n\obj), 229, 299, 0.2)
                
                If n\LastSeen Then 	
                    PointEntity n\obj2,mainPlayer\collider
                    RotateEntity n\obj, 0, CurveAngle(EntityYaw(n\obj2),EntityYaw(n\obj),40), 0
                    If dist < 4 Then n\State = Rand(1,2)
                Else
                    If dist < 6 And Rand(5)=1 Then
                        If EntityInView(n\Collider,mainPlayer\cam) Then
                            If EntityVisible(mainPlayer\collider, n\Collider) Then
                                n\LastSeen = 1
                                PlaySound_Strict LoadTempSound("SFX\SCP\513\Bell"+Rand(2,3)+".ogg")
                            EndIf
                        EndIf
                    EndIf								
                EndIf
                
            Else
                If n\Path[0]=Null Then
                    
                    ;move towards a waypoint that is:
                    ;1. max 8 units away from 513-1
                    ;2. further away from the player than 513-1's current position 
                    For w.WayPoints = Each WayPoints
                        x = Abs(EntityX(n\Collider,True)-EntityX(w\obj,True))
                        If x < 8.0 And x > 1.0 Then
                            z = Abs(EntityZ(n\Collider,True)-EntityZ(w\obj,True))
                            If z < 8.0 And z > 1.0 Then
                                If EntityDistance(mainPlayer\collider, w\obj) > dist Then
                                    n\Path[0]=w
                                    Exit
                                EndIf
                            EndIf
                        EndIf
                    Next
                    
                    ;no suitable path found -> 513-1 simply disappears
                    If n\Path[0] = Null Then
                        n\Idle = True
                        n\State2 = 0
                    EndIf
                Else
                    
                    If EntityDistance(n\Collider, n\Path[0]\obj) > 1.0 Then
                        PointEntity n\obj, n\Path[0]\obj
                        RotateEntity n\Collider, CurveAngle(EntityPitch(n\obj),EntityPitch(n\Collider),15.0), CurveAngle(EntityYaw(n\obj),EntityYaw(n\Collider),15.0), 0, True
                        n\CurrSpeed = CurveValue(0.05*Max((7.0-dist)/7.0,0.0),n\CurrSpeed,15.0)
                        MoveEntity n\Collider, 0,0,n\CurrSpeed*FPSfactor
                        If Rand(200)=1 Then MoveEntity n\Collider, 0, 0, 0.5
                        RotateEntity n\Collider, 0, EntityYaw(n\Collider), 0, True
                    Else
                        For i = 0 To 4
                            If n\Path[0]\connected[i] <> Null Then
                                If EntityDistance(mainPlayer\collider, n\Path[0]\connected[i]\obj) > dist Then
                                    
                                    If n\LastSeen = 0 Then 
                                        If EntityInView(n\Collider,mainPlayer\cam) Then
                                            If EntityVisible(mainPlayer\collider, n\Collider) Then
                                                n\LastSeen = 1
                                                PlaySound_Strict LoadTempSound("SFX\SCP\513\Bell"+Rand(2,3)+".ogg")
                                            EndIf
                                        EndIf
                                    EndIf
                                    
                                    n\Path[0]=n\Path[0]\connected[i]
                                    Exit
                                EndIf
                            EndIf
                        Next
                        
                        If n\Path[0]=Null Then n\State2 = 0
                    EndIf
                EndIf
            EndIf
            
            PositionEntity(n\obj, EntityX(n\Collider), EntityY(n\Collider) - 0.2 + Sin((MilliSecs2()/8) Mod 360)*0.1, EntityZ(n\Collider))
            
            Select n\State 
                Case 1
                    AnimateNPC(n, 458, 527, n\CurrSpeed*20)
                    ;Animate2(n\obj, AnimTime(n\obj), 458, 527, n\CurrSpeed*20)
                    RotateEntity n\obj, 0, EntityYaw(n\Collider), 0 
                Case 2
                    AnimateNPC(n, 229, 299, 0.2)
                    ;Animate2(n\obj, AnimTime(n\obj), 229, 299, 0.2)
                    RotateEntity n\obj, 0, EntityYaw(n\Collider), 0						
            End Select
            
            If n\State2 > 0 Then
                If dist < 4.0 Then n\State2 = n\State2-FPSfactor*4
                n\State2 = n\State2-FPSfactor
            Else
                n\Path[0]=Null
                n\Idle = True
                n\State2=0
            EndIf
            
        End If
        
    EndIf
    
    n\DropSpeed = 0
    ResetEntity(n\Collider)						
End Function
