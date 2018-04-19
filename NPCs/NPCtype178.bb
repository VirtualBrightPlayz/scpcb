Function InitializeNPCtype178(n.NPCs)
    n\NVName = "Unidentified"
    
    n\Collider = CreatePivot()
    EntityRadius n\Collider,0.2
    
    For n2.NPCs = Each NPCs
        If (n\NPCtype = n2\NPCtype) And (n<>n2) Then
            n\obj = CopyEntity (n2\obj)
            Exit
        EndIf
    Next
    
    If n\obj = 0 Then 
        n\obj = LoadAnimMesh_Strict("GFX\npcs\npc178.b3d")
    EndIf
    
    temp# = Rnd(0.09,0.125)
    
    EntityFX n\obj,1
    
    temp# = Rnd(0.6,0.8) / MeshWidth(n\obj)
    ScaleEntity n\obj, temp, temp, temp
    
    ;EntityColor n\obj,Rnd(0,50),0,Rnd(50,100)
    
    SetAnimTime n\obj,15.0
    
    EntityType n\Collider,HIT_178
    
    n\Speed = 0.02
End Function

Function UpdateNPCtype178(n.NPCs)
    dist# = EntityDistance(n\Collider,mainPlayer\collider)
    
    If n\Sound > 0 Then
        temp = 0.5
        ;the ambient sound gets louder when the npcs are attacking
        If n\State > 0 Then temp = 1.0	
        
        n\SoundChn = LoopSound2(n\Sound, n\SoundChn, mainPlayer\cam, mainPlayer\cam, 10.0,temp)
    EndIf
    
    temp = Rnd(-1.0,1.0)
    PositionEntity n\obj,EntityX(n\Collider,True)+n\PrevX*temp,EntityY(n\Collider,True)-0.2,EntityZ(n\Collider,True)+n\PrevZ*temp
    RotateEntity n\obj,0.0,EntityYaw(n\Collider)-180,0.0
    
    ;use the prev-values to do a "twitching" effect"
    n\PrevX = CurveValue(0.0, n\PrevX, 10.0)
    n\PrevZ = CurveValue(0.0, n\PrevZ, 10.0)
    
    If Rand(200)=1 Then
        If Rand(5)=1 Then
            n\PrevX = (EntityX(mainPlayer\collider)-EntityX(n\Collider))*0.9
            n\PrevZ = (EntityZ(mainPlayer\collider)-EntityZ(n\Collider))*0.9
        Else
            n\PrevX = Rnd(0.1,0.5)
            n\PrevZ = Rnd(0.1,0.5)						
        EndIf
        
    EndIf
    
    If (Not IsPlayerWearingTempName(mainPlayer,"scp178")) And (n\State3=0.0) Then
        If n\State<=0 Then
            ;nothing happens here...
        Else
            EntityAlpha n\obj,0.0
            n\State=Max(n\State-FPSfactor,0.0)
        EndIf
    Else
        If n\State3=0.0 Then
            EntityAlpha n\obj,0.5+(Float(Rand(0,1))/2.0)
        Else
            EntityAlpha n\obj,0.5+(Sin(n\Frame*6.0)/2.0)
        EndIf
    EndIf
    
    If n\State = 0 Then
        If n\Reload = 0 Then
            AnimateNPC(n, 206, 240, 0.1)
            If n\Frame>=240 Then n\Reload = 1
            
            ;Animate2(n\obj, AnimTime(n\obj), 206, 240, 0.1)
            ;If AnimTime(n\obj)>=240 Then n\Reload = 1
        Else
            AnimateNPC(n, 206, 240, -0.1)
            If n\Frame<=206 Then n\Reload = 0					
            
            ;Animate2(n\obj, AnimTime(n\obj), 206, 240, -0.1)
            ;If AnimTime(n\obj)<=206 Then n\Reload = 0
        EndIf
        
        ;If Rand(1,15)=1 Then EntityColor n\obj,Rand(0,50),0,Rand(50,100)
        angle = VectorYaw(EntityX(mainPlayer\collider)-EntityX(n\Collider),0,EntityZ(mainPlayer\collider)-EntityZ(n\Collider))
        RotateEntity n\Collider,0.0,CurveAngle(angle,EntityYaw(n\Collider),20.0),0.0
        
        For n2.NPCs = Each NPCs
            If (n2\NPCtype=n\NPCtype) And (n<>n2) Then
                If n2\State>0.0 Then n\State=1.0
                If n2\State3>0.0 Then n\State3=1.0
                ;if one of the npcs is hostile, make all of them hostile
            EndIf
            If n\State<>0 Then Exit
        Next
        If dist<1.0 And EntityInView(n\obj,mainPlayer\cam) Then
            n\State2=n\State2+FPSfactor
        ElseIf EntityCollided(n\Collider,HIT_PLAYER)=mainPlayer\collider Then
            n\State2=50.0
        Else
            n\State2=Max(n\State2-FPSfactor,0.0)
        EndIf
        If n\State2>=50.0 Then
            n\State=1.0
        EndIf
    EndIf
    If n\State>0 Then
        If n\State3=0 Then
            For n2.NPCs = Each NPCs
                If (n2\NPCtype=n\NPCtype) And (n<>n2) Then
                    If n2\State3>0.0 Then n\State3=1.0
                EndIf
                If n\State3<>0 Then Exit
            Next
        EndIf
        
        ;If Rand(1,15)=1 Then EntityColor n\obj,Rand(50,100),0,Rand(0,50)
        If (IsPlayerWearingTempName(mainPlayer,"scp178")) Or (n\State3>0) Then
            n\State=Min(n\State+FPSfactor,300.0)
        Else
            n\State=Max(n\State-FPSfactor,0.0)
        EndIf
        If n\PathTimer <= 0 Then
            n\PathStatus = FindPath (n, EntityX(mainPlayer\collider,True), EntityY(mainPlayer\collider,True), EntityZ(mainPlayer\collider,True))
            n\PathTimer = 40*10
            n\CurrSpeed = 0
        EndIf
        n\PathTimer = Max(n\PathTimer-FPSfactor,0)
        
        If (Not EntityVisible(n\Collider,mainPlayer\collider)) Then
            If n\PathStatus = 2 Then
                n\CurrSpeed = 0
                n\Frame = 15
                ;SetAnimTime n\obj,15
            ElseIf n\PathStatus = 1
                If n\Path[n\PathLocation]=Null Then 
                    If n\PathLocation > 19 Then 
                        n\PathLocation = 0 : n\PathStatus = 0
                    Else
                        n\PathLocation = n\PathLocation + 1
                    EndIf
                Else
                    angle = VectorYaw(EntityX(n\Path[n\PathLocation]\obj,True)-EntityX(n\Collider),0,EntityZ(n\Path[n\PathLocation]\obj,True)-EntityZ(n\Collider))
                    RotateEntity n\Collider,0.0,CurveAngle(angle,EntityYaw(n\Collider),10.0),0.0
                    
                    n\CurrSpeed = CurveValue(n\Speed,n\CurrSpeed,10.0)
                    
                    If EntityDistance(n\Collider,n\Path[n\PathLocation]\obj) < 0.2 Then
                        n\PathLocation = n\PathLocation + 1
                    EndIf 
                EndIf
            ElseIf n\PathStatus = 0
                ;SetAnimTime(n\obj,64)
                n\CurrSpeed = CurveValue(0,n\CurrSpeed,10.0)
            EndIf
        Else
            angle = VectorYaw(EntityX(mainPlayer\collider)-EntityX(n\Collider),0,EntityZ(mainPlayer\collider)-EntityZ(n\Collider))
            RotateEntity n\Collider,0.0,CurveAngle(angle,EntityYaw(n\Collider),10.0),0.0
            
            n\CurrSpeed = CurveValue(n\Speed,n\CurrSpeed,10.0)
        EndIf
        
        If dist>0.3 Then
            MoveEntity n\Collider, 0, 0, n\CurrSpeed * FPSfactor
            AnimateNPC(n, 64, 91, n\CurrSpeed*10.0)
            ;Animate2(n\obj, AnimTime(n\obj), 64, 91, n\CurrSpeed*10.0)
            
            For d.Doors = Each Doors
                If (EntityDistance(n\Collider,d\obj)<1.25) And (d\Code<>"GEAR") And (EntityCollided(n\Collider,HIT_178)=0) Then
                    n\DropSpeed=0
                    ResetEntity n\Collider
                EndIf
            Next
        EndIf
        If dist<0.4 Then
            ;Animate2(n\obj, AnimTime(n\obj), 122, 172, 0.2, False)		
            If (n\State2>=65.0) And (Not mainPlayer\dead) Then
                mainPlayer\injuries=mainPlayer\injuries+Rnd(0.4,0.8)
                If mainPlayer\injuries>=4.0 Then
                    If n\State3=0.0 Then
                        DeathMSG = "A Class D subject wearing SCP-178 was found dead in [REDACTED]. "
                        DeathMSG = DeathMSG + "Presumed to be killed by the "
                        DeathMSG = DeathMSG + "effects of SCP-178."
                    Else
                        DeathMSG = "Several bipedal entities of unknown origin were found in Site-[REDACTED]. "
                        DeathMSG = DeathMSG + "Testing reveals that these entities behave similarly to those "
                        DeathMSG = DeathMSG + "seen when using SCP-178. It is unknown at the current time "
                        DeathMSG = DeathMSG + "whether SCP-178 and these entities are related."
                    EndIf
                    Kill(mainPlayer)
                EndIf
                PlaySound_Strict DamageSFX(Rand(2,3))
                n\State2=0.0
            Else
                n\State2=n\State2+FPSfactor
            EndIf
        EndIf
        
    EndIf
End Function
;~IDEal Editor Parameters:
;~C#Blitz3D