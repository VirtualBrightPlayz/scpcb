cmake_minimum_required(VERSION 3.10)

project(BuildShaders)

# specify the C++ standard
set(CMAKE_CXX_STANDARD 17)
set(CMAKE_CXX_STANDARD_REQUIRED True)

# add_subdirectory(Engine)

file(GLOB_RECURSE BS_SOURCE_FILES
    "${CMAKE_CURRENT_SOURCE_DIR}/Engine/Windows/BuildShaders/*.cpp"
    )
foreach(f ${BS_SOURCE_FILES})
    if (NOT f MATCHES "Init.cpp")
        list(APPEND ${ret_val} ${f})
    endif()
endforeach(f)

add_executable(BuildShaders "${CMAKE_CURRENT_SOURCE_DIR}/Engine/Windows/BuildShaders/BuildShaders.cpp")
target_sources(BuildShaders PUBLIC "${ret_val}")

# add_compile_definitions(SDL_MAIN_HANDLED)
# add_compile_definitions(SDL_MAIN_NEEDED)

set_target_properties(BuildShaders PROPERTIES LINKER_LANGUAGE CXX)

target_link_libraries(BuildShaders PUBLIC Engine)
target_link_libraries(BuildShaders PUBLIC d3d11 dxgi d3dcompiler dxguid)

target_include_directories(BuildShaders
PUBLIC
    "${CMAKE_CURRENT_SOURCE_DIR}/Engine/Windows/BuildShaders"
    "${CMAKE_CURRENT_SOURCE_DIR}/Engine/Include"
    )