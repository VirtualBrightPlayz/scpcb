
; LoadTexture Example
; -------------------

Graphics3D(640,480)
SetBuffer(BackBuffer())

Local camera% = CreateCamera()

Local light%=CreateLight()
RotateEntity(light,90,0,0)

Local cube%=CreateCube()
PositionEntity(cube,0,0,5)

; Load texture
Local tex% = LoadTexture( "media/b3dlogo.jpg" )

; Texture cube with texture
EntityTexture(cube,tex)

While (Not KeyDown( 1 ))
    Local pitch#=0
    Local yaw#=0
    Local roll#=0

    If (KeyDown(208)) Then pitch=-1
    If (KeyDown(200)) Then pitch=1
    If (KeyDown(203)) Then yaw=-1
    If (KeyDown(205)) Then yaw=1
    If (KeyDown(45)) Then roll=-1
    If (KeyDown(44)) Then roll=1

    TurnEntity(cube, pitch, yaw, roll)

    RenderWorld()
    Flip()
Wend

End()

Function Ligger%(for1#, for2#)
    Local retFor# = for1 / for2
    Return retFor
End Function