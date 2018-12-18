package id.co.kynga.app.general.controller;


import id.co.kynga.app.general.controller.url.URLController;
import id.co.kynga.app.general.controller.url.URLManager;
import id.co.kynga.app.model.GameModel;
import id.co.kynga.app.model.RadioModel;
import id.co.kynga.app.model.ResponseModel;
import id.co.kynga.app.model.TVModel;
import id.co.kynga.app.model.VideoCategoryModel;
import id.co.kynga.app.model.VideoModel;
import id.co.kynga.app.model.YoutubePlaylistModel;

public class RequestController {
	public interface VideoGroupCallback {
		void didVideoGroupSucceeded(final VideoCategoryModel video_category_model);
		void didVideoGroupFailed();
	}

	public interface VideoCategoryCallback {
		void didVideoCategorySucceeded(final VideoModel video_model);
		void didVideoCategoryFailed();
	}

	public interface TVCategoryCallback {
		void didTVCategorySucceeded(final TVModel tv_model);
		void didTVCategoryFailed();
	}

	public interface YoutubeChannelCallback {
		void didYoutubeChannelSucceeded(final YoutubePlaylistModel youtube_playlist_model);
		void didYoutubeChannelFailed();
	}

	public interface RadioCategoryCallback {
		void didRadioCategorySucceeded(final RadioModel radio_model);
		void didRadioCategoryFailed();
	}
	public interface GameCategoryCallback {
		void didGameCategorySucceeded(final GameModel game_model);
		void didGameCategoryFailed();
	}

	private static VideoGroupCallback video_group_callback;
	private static VideoCategoryCallback video_category_callback;
	private static TVCategoryCallback tv_category_callback;
	private static YoutubeChannelCallback youtube_channel_callback;
	private static RadioCategoryCallback radio_category_callback;
	private static GameCategoryCallback game_category_callback;

	public static void VideoGroupRequest(
			final long video_group_id,
			final VideoGroupCallback vgc) {
		video_group_callback = vgc;
		URLController.videoGroupAppMStar(video_group_id, new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, String response) {
				setVideoGroupValidation(response);
			}

			@Override
			public void didURLFailed() {
				if (video_group_callback != null) {
					video_group_callback.didVideoGroupFailed();
				}
			}
		});
	}

	private static void setVideoGroupValidation(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			final VideoCategoryModel video_category_model = new VideoCategoryModel(response_model.Result);
			if (video_group_callback != null) {
				video_group_callback.didVideoGroupSucceeded(video_category_model);
			}
		} else {
			if (video_group_callback != null) {
				video_group_callback.didVideoGroupFailed();
			}
		}
	}

	public static void VideoCategoryRequest(
			final long video_category_id,
			final VideoCategoryCallback vcc) {
		video_category_callback = vcc;
		URLController.videoCategoryAppMStar(video_category_id, new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, String response) {
				setVideoCategoryValidation(response);
			}

			@Override
			public void didURLFailed() {
				if (video_category_callback != null) {
					video_category_callback.didVideoCategoryFailed();
				}
			}
		});
	}

	private static void setVideoCategoryValidation(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			final VideoModel video_model = new VideoModel(response_model.Result);
			if (video_category_callback != null) {
				video_category_callback.didVideoCategorySucceeded(video_model);
			}
		} else {
			if (video_category_callback != null) {
				video_category_callback.didVideoCategoryFailed();
			}
		}
	}

	public static void TVCategoryRequestAppMStar(
			final long tv_category_id,
			final TVCategoryCallback tcc) {

		tv_category_callback = tcc;
		URLController.tvCategoryAppMStar(tv_category_id, new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, String response) {
				//GlobalController.showToast(response, 20000);
				setTVCategoryValidationAppMStar(response);
			}

			@Override
			public void didURLFailed() {
				if (tv_category_callback != null) {
					tv_category_callback.didTVCategoryFailed();
				}
			}
		});

	}

	private static void setTVCategoryValidationAppMStar(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			final TVModel tv_model = new TVModel(response_model.Result);
			if (tv_category_callback != null) {
				tv_category_callback.didTVCategorySucceeded(tv_model);
			}
		} else {
			if (tv_category_callback != null) {
				tv_category_callback.didTVCategoryFailed();
			}
		}
	}

	public static void TVCategoryRequest(
			final long tv_category_id,
			final TVCategoryCallback tcc) {
		tv_category_callback = tcc;
		URLController.tvCategory(tv_category_id, new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, String response) {
				setTVCategoryValidation(response);
			}

			@Override
			public void didURLFailed() {
				if (tv_category_callback != null) {
					tv_category_callback.didTVCategoryFailed();
				}
			}
		});
	}

	private static void setTVCategoryValidation(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			final TVModel tv_model = new TVModel(response_model.Result);
			if (tv_category_callback != null) {
				tv_category_callback.didTVCategorySucceeded(tv_model);
			}
		} else {
			if (tv_category_callback != null) {
				tv_category_callback.didTVCategoryFailed();
			}
		}
	}

	public static void YoutubeChannelRequestAppMStar(
			final String youtube_channel_id,
			final YoutubeChannelCallback ycc) {
		youtube_channel_callback = ycc;
		URLController.youtubeChannelAppMStar(youtube_channel_id, new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, String response) {
				setYoutubeChannelValidation(response);
			}

			@Override
			public void didURLFailed() {
				if (youtube_channel_callback != null) {
					youtube_channel_callback.didYoutubeChannelFailed();
				}
			}
		});
	}

	private static void setYoutubeChannelValidation(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			final YoutubePlaylistModel youtube_playlist_model = new YoutubePlaylistModel(response_model.Result);
			if (youtube_channel_callback != null) {
				youtube_channel_callback.didYoutubeChannelSucceeded(youtube_playlist_model);
			}
		} else {
			if (youtube_channel_callback != null) {
				youtube_channel_callback.didYoutubeChannelFailed();
			}
		}
	}

	public static void RadioCategoryRequestAppMStar(
			final long radio_category_id,
			final RadioCategoryCallback rcc) {
		radio_category_callback = rcc;
		URLController.radioCategoryAppMStar(radio_category_id, new URLManager.URLCallback() {
			@Override
			public void didURLSucceeded(int status_code, String response) {
				setRadioCategoryValidation(response);
			}

			@Override
			public void didURLFailed() {
				if (youtube_channel_callback != null) {
					youtube_channel_callback.didYoutubeChannelFailed();
				}
			}
		});
	}

	private static void setRadioCategoryValidation(final String response) {
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType) {
			final RadioModel radio_model = new RadioModel(response_model.Result);
			if (radio_category_callback != null) {
				radio_category_callback.didRadioCategorySucceeded(radio_model);
			}
		} else {
			if (radio_category_callback != null) {
				radio_category_callback.didRadioCategoryFailed();
			}
		}
	}

	private static void setGameCategoryValidation(final String response){
		final ResponseModel response_model = new ResponseModel(response);
		if (response_model.Status == ResponseModel.StatusType.SucceededStatusType){
			final GameModel game_model  = new GameModel(response_model.Result);
			if (game_category_callback != null){
				game_category_callback.didGameCategorySucceeded(game_model);
			}

		}else {
			if (game_category_callback != null) {
				game_category_callback.didGameCategoryFailed();
			}
		}

	}
}