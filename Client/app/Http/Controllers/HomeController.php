<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Validator;
use App\Models\User;
use Illuminate\Support\Str;

class HomeController extends Controller
{
    public function index(Request $request)
    {
        if ($request->has('open_id') == false || $request->has('world_id') == false) {
            return redirect('login');
        }
        return view('home');
    }

    public function mobile(Request $request)
    {
        if ($request->has('open_id') == false || $request->has('world_id') == false) {
            return redirect('login');
        }
        return view('mobile');
    }
}
