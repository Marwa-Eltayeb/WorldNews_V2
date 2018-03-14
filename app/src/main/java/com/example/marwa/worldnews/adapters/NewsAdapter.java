package com.example.marwa.worldnews.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.marwa.worldnews.News;
import com.example.marwa.worldnews.R;

import java.io.InputStream;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by Marwa on 1/22/2018.
 */

public class NewsAdapter extends ArrayAdapter<News> {

    /**
     * Construct a new {@link NewsAdapter}
     *
     * @param context of the app
     * @param news    is the list of news stories which is the data source of the adapter
     */
    public NewsAdapter(Context context, ArrayList<News> news) {
        super(context, 0, news);
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        // Check if there is an existing list item view{called convertView} that we can
        // reuse, otherwise, if convertView is null, then inflate a new list item layout
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.news_list_item, parent, false);
        }

        // find the news story at the given position in the list of news stories.
        News currentNewsArticle = getItem(position);

        // Find the TextView with view ID title.
        TextView title = (TextView) listItemView.findViewById(R.id.title_text_view);
        // Display the title of the current News in that TextView.
        title.setText(currentNewsArticle.getTitle());

        // Find the TextView with view ID section.
        TextView section = (TextView) listItemView.findViewById(R.id.section_text_view);
        //Display the author of the current News in that TextView/
        section.setText(currentNewsArticle.getSection());

        // Find the TextView with view ID publication Date.
        TextView publicationDate = (TextView) listItemView.findViewById(R.id.publicationDate_text_view);
        // Format the date string.
        String formattedDate = formatDate(currentNewsArticle.getPublicationDate());
        // Display the date of the current News story in that TextView.
        publicationDate.setText(formattedDate);

        //Find the TextView with view ID author name.
        TextView authorName = (TextView) listItemView.findViewById(R.id.authorName_text_view);
        //Display the author of the current News in that TextView.
        String author = currentNewsArticle.getAuthorName();
        if (author.equals("")) {
            author = getContext().getResources().getString(R.string.unnamed);
        }
        authorName.setText(author);

        //Find the TextView with view ID description.
        TextView description = (TextView) listItemView.findViewById(R.id.description_text_view);
        //Display the description of the current news story in that TextView.
        description.setText(currentNewsArticle.getShortDescription());

        // Find the ImageView in the list_item.xml layout with the ID image.
        ImageView imageView = (ImageView) listItemView.findViewById(R.id.imageOfNewsArticle_text_view);
        // Put the image resource in a String Variable.
        String imageUrl = currentNewsArticle.getImageResource();

        // Check if an image is provided for this news story or not
        if (currentNewsArticle.hasImage()) {
            //Display the image of the current news story in that ImageView
            new DownLoadImageTask(imageView).execute(imageUrl);
        } else {
            // Otherwise Set the ImageView to the default image.
            imageView.setImageResource(R.drawable.news);
        }

        return listItemView;
    }

    // Format the date received from guardian JSON
    private static String formatDate(String currentDate) {
        // This is the time format from guardian JSON "2017-10-29T06:00:20Z"
        // will be changed to 29-10-2017 8pm format
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date newDate = format.parse(currentDate);
            format = new SimpleDateFormat("dd-MM-yyyy, h:mm a");
            currentDate = format.format(newDate);
        } catch (ParseException e) {
            Log.e("Adapter", "Problem with parsing the date format");
        }
        return currentDate;
    }

    // Inner Class for downloading images
    private class DownLoadImageTask extends AsyncTask<String, Void, Bitmap> {

        ImageView imageView;

        DownLoadImageTask(ImageView imageView) {

            this.imageView = imageView;
        }

        protected Bitmap doInBackground(String... urls) {
            String urlOfImage = urls[0];
            Bitmap logo = null;
            try {
                InputStream is = new URL(urlOfImage).openStream();
                logo = BitmapFactory.decodeStream(is);
            } catch (Exception e) {
                // Catch the download exception
                Log.v("download", e.getMessage());

            }
            return logo;
        }

        protected void onPostExecute(Bitmap result) {
            imageView.setImageBitmap(result);
        }
    }

}
