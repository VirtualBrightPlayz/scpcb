Function InitializeNPCtype1048a(n.NPCs)
    n\NVName = "SCP-1048-A"
    n\obj =	LoadAnimMesh_Strict("GFX\npcs\scp-1048a.b3d")
    ScaleEntity n\obj, 0.05,0.05,0.05
    SetAnimTime(n\obj, 2)
    
    n\Sound = LoadSound_Strict("SFX\SCP\1048A\Shriek.ogg")
    n\Sound2 = LoadSound_Strict("SFX\SCP\1048A\Growth.ogg")
End Function

Function UpdateNPCtype1048a(n.NPCs)
    Select n\State	
        Case 1
            n\PathStatus = FindPath(n, n\EnemyX,n\EnemyY+0.1,n\EnemyZ)
            ;649, 677
    End Select
End Function
