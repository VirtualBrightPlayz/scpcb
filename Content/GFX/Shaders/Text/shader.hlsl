Texture2D tex0;
SamplerState smp;

cbuffer cbMatrices {
    matrix modelMatrix;
    matrix projectionMatrix;
};

cbuffer cbFragment {
    float4 imageColor;
};

struct VS_INPUT {
    float3 position  : POSITION0;
    float2 uv : TEXCOORD0;
};

struct PS_INPUT {
    float4 position  : SV_POSITION;
    float2 uv : TEXCOORD0;
};

struct PS_OUTPUT {
    float4 color : SV_Target0;
};

PS_INPUT VS(VS_INPUT input) {
    PS_INPUT output = (PS_INPUT)0;
    output.position = float4(input.position.x, input.position.y, input.position.z, 1.0);
    output.position = mul(output.position, modelMatrix);
    output.position = mul(output.position, projectionMatrix);
    output.uv = input.uv;
    return output;
}

PS_OUTPUT PS(PS_INPUT input) {
    PS_OUTPUT output = (PS_OUTPUT)0;
    float4 diffColor = tex0.Sample(smp, input.uv);
    output.color = imageColor * diffColor;
    return output;
}
