Function InitializeNPCtype1048a(n.NPCs)
    n\NVName = "SCP-1048-A"
    n\obj =	LoadAnimMesh("GFX\npcs\scp-1048a.b3d")
    ScaleEntity n\obj, 0.05,0.05,0.05
    SetAnimTime(n\obj, 2)
    
    n\sounds[0] = LoadSound("SFX\SCP\1048A\Shriek.ogg")
    n\sounds[1] = LoadSound("SFX\SCP\1048A\Growth.ogg")
End Function

Function UpdateNPCtype1048a(n.NPCs)
    Select n\State	
        Case 1
            n\PathStatus = FindPath(n, n\EnemyX,n\EnemyY+0.1,n\EnemyZ)
            ;649, 677
    End Select
End Function
