Function UpdateNVG()
    Local wornItem.Item = mainPlayer\inventory\items[WORNITEM_SLOT_HEAD]

    If (wornItem <> Null) Then
        If (wornItem\template\name <> "nvgoggles" And wornItem\template\name <> "supernv") Then
            wornItem = Null
        EndIf
    EndIf

    Local decayMultiplier# = 0.02 ;TODO: idfk
    If (wornItem<>Null) Then
        wornItem\state = Max(-1.0,wornItem\state - (timing\tickDuration * decayMultiplier))
    EndIf
End Function

;~IDEal Editor Parameters:
;~C#Blitz3D