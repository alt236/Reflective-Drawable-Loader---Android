/*******************************************************************************
 * Copyright 2013 alex
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
package co.uk.alt236.reflectivedrawableloader.sampleapp.util;

import java.util.ArrayList;
import java.util.Random;

import android.content.Context;
import co.uk.alt236.reflectivedrawableloader.ReflectiveDrawableLoader;
import co.uk.alt236.reflectivedrawableloader.containers.DrawableResourceContainer;
import co.uk.alt236.reflectivedrawableloader.sampleapp.R;

public class IconArray {
    public static String[] ICON_ARRAY = {
	"social_share",
	"social_send_now",
	"social_reply_all",
	"social_reply",
	"social_person",
	"social_group",
	"social_forward",
	"social_chat",
	"social_cc_bcc",
	"social_add_person",
	"social_add_group",
	"rating_not_important",
	"rating_important",
	"rating_half_important",
	"rating_good",
	"rating_favorite",
	"rating_bad",
	"navigation_refresh",
	"navigation_previous_item",
	"navigation_next_item",
	"navigation_forward",
	"navigation_expand",
	"navigation_collapse",
	"navigation_cancel",
	"navigation_back",
	"navigation_accept",
	"location_web_site",
	"location_place",
	"location_map",
	"location_directions",
	"images_slideshow",
	"images_rotate_right",
	"images_rotate_left",
	"images_crop",
	"hardware_phone",
	"hardware_mouse",
	"hardware_keyboard",
	"hardware_headset",
	"hardware_headphones",
	"hardware_gamepad",
	"hardware_dock",
	"hardware_computer",
	"device_access_volume_on",
	"device_access_volume_muted",
	"device_access_video",
	"device_access_usb",
	"device_access_time",
	"device_access_switch_video",
	"device_access_switch_camera",
	"device_access_storage",
	"device_access_secure",
	"device_access_sd_storage",
	"device_access_screen_rotation",
	"device_access_screen_locked_to_portrait",
	"device_access_screen_locked_to_landscape",
	"device_access_ring_volume",
	"device_access_not_secure",
	"device_access_new_account",
	"device_access_network_wifi",
	"device_access_network_cell",
	"device_access_mic_muted",
	"device_access_mic",
	"device_access_location_searching",
	"device_access_location_off",
	"device_access_location_found",
	"device_access_flash_on",
	"device_access_flash_off",
	"device_access_flash_automatic",
	"device_access_end_call",
	"device_access_dial_pad",
	"device_access_data_usage",
	"device_access_camera",
	"device_access_call",
	"device_access_brightness_medium",
	"device_access_brightness_high",
	"device_access_brightness_auto",
	"device_access_bluetooth_searching",
	"device_access_bluetooth_connected",
	"device_access_bluetooth",
	"device_access_bightness_low",
	"device_access_battery",
	"device_access_alarms",
	"device_access_add_alarm",
	"device_access_accounts",
	"content_unread",
	"content_undo",
	"content_split",
	"content_select_all",
	"content_save",
	"content_remove",
	"content_read",
	"content_picture",
	"content_paste",
	"content_new_picture",
	"content_new_event",
	"content_new_email",
	"content_new_attachment",
	"content_new",
	"content_merge",
	"content_import_export",
	"content_event",
	"content_email",
	"content_edit",
	"content_discard",
	"content_cut",
	"content_copy",
	"content_backspace",
	"content_attachment",
	"collections_view_as_list",
	"collections_view_as_grid",
	"collections_sort_by_size",
	"collections_new_label",
	"collections_labels",
	"collections_go_to_today",
	"collections_collection",
	"collections_cloud",
	"av_upload",
	"av_stop",
	"av_shuffle",
	"av_rewind",
	"av_return_from_full_screen",
	"av_replay",
	"av_repeat",
	"av_previous",
	"av_play_over_video",
	"av_play",
	"av_pause_over_video",
	"av_pause",
	"av_next",
	"av_make_available_offline",
	"av_full_screen",
	"av_fast_forward",
	"av_download",
	"av_add_to_queue",
	"alerts_and_states_warning",
	"alerts_and_states_error",
	"alerts_and_states_airplane_mode_on",
	"alerts_and_states_airplane_mode_off",
	"action_settings",
	"action_search",
	"action_help",
	"action_about"
    };
    
    public static String[] FAMILY_ARRAY = {
	"light",
	"dark",
    };
    
    public static ArrayList<String> getSimpleDrawableList(int resultSize){
	ArrayList<String> tmpList = new ArrayList<String>();
	
	Random r = new Random();
	
	for(int i = 0; i < resultSize; i++){
	    tmpList.add( ICON_ARRAY[r.nextInt(ICON_ARRAY.length)]);
	}
	
	return tmpList;
    }
    
    public static ArrayList<DrawableResourceContainer> getColorisedDrawableList(Context context, int resultSize){
	ArrayList<DrawableResourceContainer> tmpList = new ArrayList<DrawableResourceContainer>();
	ReflectiveDrawableLoader loader = ReflectiveDrawableLoader.getInstance(context);
	RandomColorGenerator colorGen = new RandomColorGenerator();
	
	Random r = new Random();
	DrawableResourceContainer resourceContainer;
	String name;
	String family;
	String colour;
	
	loader.setAddDrawableNameToContainer(true);
	loader.setLogErrors(true);
	
	for(int i = 0; i < resultSize; i++){
	    name = ICON_ARRAY[r.nextInt(ICON_ARRAY.length)];
	    family = FAMILY_ARRAY[r.nextInt(FAMILY_ARRAY.length)];
	    colour = colorGen.getRandomHexColor();
	    resourceContainer = loader.getMenuDrawableContainer(
		    name, 
		    family, 
		    colour, 
		    R.drawable.ic_missing_icon);
	    
	    tmpList.add(resourceContainer);
	}
	
	return tmpList;
    }
}
